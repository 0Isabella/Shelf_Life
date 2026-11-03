package com.example.shelflife.data.repository

import com.example.shelflife.data.local.AmigoDao
import com.example.shelflife.model.Amigo
import kotlinx.coroutines.flow.Flow

/**
 * Única fonte de verdade para Amigos.
 * Só ela fala com o AmigoDao; o ViewModel só fala com ela.
 *
 * CONTRATO (o ViewModel depende disso — se mudar, avise a dupla):
 *   buscarTodos(): Flow<List<Amigo>>
 *   buscarPorId(id): Amigo?
 *   inserir(amigo): Long
 *   atualizar(amigo)
 *   deletar(amigo)
 */
class AmigoRepository(private val amigoDao: AmigoDao) {

    fun buscarTodos(): Flow<List<Amigo>> = amigoDao.buscarTodos()

    suspend fun buscarPorId(id: Int): Amigo? = amigoDao.buscarPorId(id)

    suspend fun inserir(amigo: Amigo): Long = amigoDao.inserir(amigo)

    suspend fun atualizar(amigo: Amigo) = amigoDao.atualizar(amigo)

    suspend fun deletar(amigo: Amigo) = amigoDao.deletar(amigo)
}
