package com.example.shelflife.data.repository

import com.example.shelflife.data.local.LivroDao
import com.example.shelflife.model.Livro
import kotlinx.coroutines.flow.Flow

/**
 * Única fonte de verdade para Livros.
 * Só ela fala com o LivroDao; o ViewModel só fala com ela.
 *
 * CONTRATO (o ViewModel depende disso — se mudar, avise a dupla):
 *   buscarTodos(): Flow<List<Livro>>
 *   buscarPorId(id): Livro?
 *   inserir(livro): Long
 *   atualizar(livro)
 *   deletar(livro)
 */
class LivroRepository(private val livroDao: LivroDao) {

    fun buscarTodos(): Flow<List<Livro>> = livroDao.buscarTodos()

    suspend fun buscarPorId(id: Int): Livro? = livroDao.buscarPorId(id)

    suspend fun inserir(livro: Livro): Long = livroDao.inserir(livro)

    suspend fun atualizar(livro: Livro) = livroDao.atualizar(livro)

    suspend fun deletar(livro: Livro) = livroDao.deletar(livro)
}
