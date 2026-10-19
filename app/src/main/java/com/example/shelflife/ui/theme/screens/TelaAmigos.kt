package com.example.shelflife.ui.theme.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelflife.R
import com.example.shelflife.model.Amigo
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import org.json.JSONArray

private val UsuariosDoApp = listOf(
    Amigo(0, "Ovin", 5400, "🐣", R.drawable.amigo_0, livrosLidos = 12, sequenciaDias = 21),
    Amigo(1, "Cogumelito", 8550, "🍄", R.drawable.amigo_1, livrosLidos = 7, sequenciaDias = 4),
    Amigo(2, "Sapinho", 3000, "🐸", R.drawable.amigo_2, livrosLidos = 15, sequenciaDias = 1),
    Amigo(3, "Nome", 1200, "🙂", R.drawable.amigo_3, livrosLidos = 3, sequenciaDias = 9),
    Amigo(4, "Raposa", 4300, "🦊", R.drawable.amigo_4, livrosLidos = 9, sequenciaDias = 6),
    Amigo(5, "Gatinha", 6100, "🐱", R.drawable.amigo_5, livrosLidos = 11, sequenciaDias = 12),
    Amigo(6, "Lua", 2700, "🌙", R.drawable.amigo_6, livrosLidos = 5, sequenciaDias = 3),
    Amigo(7, "Livreiro", 9800, "📚", R.drawable.amigo_7, livrosLidos = 24, sequenciaDias = 40)
)

object AmigosStorage {
    private const val ARQUIVO = "shelflife_amigos"
    private const val CHAVE = "ids_amigos"

    private fun prefs(context: Context) =
        context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE)

    fun carregar(context: Context): List<Amigo> {
        val texto = prefs(context).getString(CHAVE, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(texto)
            List(array.length()) { array.getInt(it) }
        }.getOrDefault(emptyList())
            .mapNotNull { id -> UsuariosDoApp.firstOrNull { it.id == id } }
    }

    fun salvar(context: Context, amigos: List<Amigo>) {
        prefs(context).edit()
            .putString(CHAVE, JSONArray(amigos.map { it.id }).toString())
            .apply()
    }

    fun carregarNota(context: Context, amigoId: Int): String =
        prefs(context).getString("nota_$amigoId", "") ?: ""

    fun salvarNota(context: Context, amigoId: Int, nota: String) {
        prefs(context).edit().putString("nota_$amigoId", nota).apply()
    }
}

@Composable
fun TelaAmigos(
    amigos: List<Amigo>,
    aoAdicionar: (Amigo) -> Unit,
    aoRemover: (Amigo) -> Unit,
    aoAbrirDetalhe: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var busca by remember { mutableStateOf("") }

    val termo = busca.trim()
    val resultados = if (termo.isEmpty()) {
        emptyList()
    } else {
        UsuariosDoApp.filter { it.nome.contains(termo, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CorFundo)
            .imePadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Amigos",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Procure alguém pelo nome e adicione à sua lista",
                    color = CorTextoSecundario,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = busca,
                    onValueChange = { if (it.length <= 30) busca = it },
                    label = { Text(text = "Buscar usuário") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    trailingIcon = {
                        if (busca.isNotEmpty()) {
                            IconButton(onClick = { busca = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpar busca",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CorDestaque,
                        unfocusedBorderColor = CorTextoSecundario,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = CorDestaque,
                        unfocusedLabelColor = CorTextoSecundario,
                        cursorColor = CorDestaque
                    )
                )
            }
        }

        if (termo.isEmpty()) {
            item {
                Text(
                    text = "Digite um nome para encontrar alguém.",
                    color = CorTextoSecundario,
                    fontSize = 14.sp
                )
            }
        } else if (resultados.isEmpty()) {
            item {
                Text(
                    text = "Nenhum usuário encontrado para \"$termo\".",
                    color = CorTextoSecundario,
                    fontSize = 14.sp
                )
            }
        } else {
            items(resultados, key = { "busca-${it.id}" }) { usuario ->
                val jaAmigo = amigos.any { it.id == usuario.id }
                CartaoUsuario(usuario = usuario) {
                    if (jaAmigo) {
                        Text(text = "Amigo ✓", color = CorTextoSecundario, fontSize = 13.sp)
                    } else {
                        Button(
                            onClick = { aoAdicionar(usuario) },
                            colors = ButtonDefaults.buttonColors(containerColor = CorDestaque)
                        ) {
                            Text(text = "Adicionar", color = Color.White)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Seus amigos (${amigos.size})",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (amigos.isEmpty()) {
            item {
                Text(
                    text = "Você ainda não adicionou ninguém.",
                    color = CorTextoSecundario,
                    fontSize = 14.sp
                )
            }
        } else {
            items(amigos, key = { "amigo-${it.id}" }) { amigo ->
                CartaoUsuario(usuario = amigo, aoClicar = { aoAbrirDetalhe(amigo.id) }) {
                    IconButton(onClick = { aoRemover(amigo) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remover ${amigo.nome}",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalheAmigo(
    amigo: Amigo?,
    amigos: List<Amigo>,
    aoVoltar: () -> Unit,
    aoRemover: (Amigo) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var nota by remember(amigo?.id) {
        mutableStateOf(amigo?.let { AmigosStorage.carregarNota(context, it.id) } ?: "")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CorFundo)
    ) {
        TopAppBar(
            title = { Text(text = amigo?.nome ?: "Amigo") },
            navigationIcon = {
                IconButton(onClick = aoVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CorCartao,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        if (amigo == null) {
            Text(
                text = "Amigo não encontrado na sua lista.",
                color = CorTextoSecundario,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AvatarUsuario(usuario = amigo, tamanho = 96)
                Text(
                    text = amigo.nome,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                CartaoInfo(titulo = "Leitura") {
                    LinhaInfo("Tempo total", tempoLegivel(amigo.minutosLidos))
                    LinhaInfo("Livros lidos", "${amigo.livrosLidos}")
                    LinhaInfo("Sequência", "${amigo.sequenciaDias} dias seguidos")
                }

                val mediaPorLivro = if (amigo.livrosLidos > 0) {
                    tempoLegivel(amigo.minutosLidos / amigo.livrosLidos)
                } else {
                    "—"
                }
                CartaoInfo(titulo = "Entre os seus ${amigos.size} amigos") {
                    LinhaInfo("Tempo médio por livro", mediaPorLivro)
                    LinhaInfo("Posição em tempo", posicao(amigos, amigo) { it.minutosLidos })
                    LinhaInfo("Posição em livros", posicao(amigos, amigo) { it.livrosLidos })
                    LinhaInfo("Posição em sequência", posicao(amigos, amigo) { it.sequenciaDias })
                }

                CartaoInfo(titulo = "Sua nota sobre ${amigo.nome}") {
                    OutlinedTextField(
                        value = nota,
                        onValueChange = { if (it.length <= 200) nota = it },
                        label = { Text(text = "Ex.: indicou um livro bom") },
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CorDestaque,
                            unfocusedBorderColor = CorTextoSecundario,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = CorDestaque,
                            unfocusedLabelColor = CorTextoSecundario,
                            cursorColor = CorDestaque
                        )
                    )
                    Button(
                        onClick = {
                            AmigosStorage.salvarNota(context, amigo.id, nota.trim())
                            Toast.makeText(context, "Nota salva!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CorDestaque)
                    ) {
                        Text(text = "Salvar nota", color = Color.White)
                    }
                }

                OutlinedButton(
                    onClick = {
                        aoRemover(amigo)
                        Toast.makeText(
                            context,
                            "${amigo.nome} removido dos amigos",
                            Toast.LENGTH_SHORT
                        ).show()
                        aoVoltar()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Remover amigo", color = Color.White)
                }
            }
        }
    }
}

private fun tempoLegivel(minutos: Int): String = "${minutos / 60}h ${minutos % 60}min"

private fun posicao(todos: List<Amigo>, amigo: Amigo, criterio: (Amigo) -> Int): String {
    val posicao = todos.sortedByDescending(criterio).indexOfFirst { it.id == amigo.id } + 1
    return "${posicao}º de ${todos.size}"
}

@Composable
private fun AvatarUsuario(usuario: Amigo, tamanho: Int) {
    Box(
        modifier = Modifier
            .size(tamanho.dp)
            .clip(CircleShape)
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        if (usuario.imagemRes != null) {
            Image(
                painter = painterResource(id = usuario.imagemRes),
                contentDescription = "Foto de ${usuario.nome}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(text = usuario.emoji, fontSize = (tamanho * 0.45f).sp)
        }
    }
}

@Composable
private fun CartaoInfo(titulo: String, conteudo: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CorCartao)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            conteudo()
        }
    }
}

@Composable
private fun LinhaInfo(rotulo: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = rotulo, color = CorTextoSecundario, fontSize = 14.sp)
        Text(text = valor, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CartaoUsuario(
    usuario: Amigo,
    aoClicar: (() -> Unit)? = null,
    acao: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (aoClicar != null) Modifier.clickable(onClick = aoClicar) else Modifier),
        colors = CardDefaults.cardColors(containerColor = CorCartao)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AvatarUsuario(usuario = usuario, tamanho = 48)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = usuario.nome,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${tempoLegivel(usuario.minutosLidos)} · ${usuario.livrosLidos} livros",
                    color = CorTextoSecundario,
                    fontSize = 13.sp
                )
            }
            acao()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviaTelaAmigos() {
    TelaAmigos(
        amigos = emptyList(),
        aoAdicionar = {},
        aoRemover = {},
        aoAbrirDetalhe = {}
    )
}