package com.example.shelflife.data

import android.content.Context
import androidx.core.content.edit
import com.example.shelflife.model.Livro
import org.json.JSONArray
import org.json.JSONObject

object LivrosStorage {
    private const val ARQUIVO = "shelflife_livros"
    private const val CHAVE = "livros"

    private fun prefs(context: Context) =
        context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE)

    fun carregar(context: Context): List<Livro> {
        val texto = prefs(context).getString(CHAVE, null) ?: return LivrosIniciais
        return runCatching {
            val array = JSONArray(texto)
            List(array.length()) { i ->
                val o = array.getJSONObject(i)
                val id = o.getInt("id")
                Livro(
                    id = id,
                    titulo = o.getString("titulo"),
                    autor = o.getString("autor"),
                    paginasTotal = o.getInt("paginasTotal"),
                    paginasLidas = o.getInt("paginasLidas"),
                    minutosLidos = o.getInt("minutosLidos"),
                    sinopse = o.getString("sinopse"),
                    anotacao = o.getString("anotacao"),
                    // O id do drawable muda entre builds, então não é salvo:
                    // a capa volta a partir dos livros iniciais.
                    imagemRes = LivrosIniciais.firstOrNull { it.id == id }?.imagemRes
                )
            }
        }.getOrDefault(LivrosIniciais)
    }

    fun salvar(context: Context, livros: List<Livro>) {
        val array = JSONArray()
        livros.forEach { livro ->
            array.put(
                JSONObject()
                    .put("id", livro.id)
                    .put("titulo", livro.titulo)
                    .put("autor", livro.autor)
                    .put("paginasTotal", livro.paginasTotal)
                    .put("paginasLidas", livro.paginasLidas)
                    .put("minutosLidos", livro.minutosLidos)
                    .put("sinopse", livro.sinopse)
                    .put("anotacao", livro.anotacao)
            )
        }
        prefs(context).edit { putString(CHAVE, array.toString()) }
    }
}