package com.example.shelflife.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shelflife.model.Amigo

@Dao
interface AmigoDao {
    @Query("SELECT * FROM amigos")
    suspend fun buscarTodos(): List<Amigo>

    @Query("SELECT * FROM amigos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Amigo?

    @Insert
    suspend fun inserir(amigo: Amigo): Long

    @Update
    suspend fun atualizar(amigo: Amigo)

    @Delete
    suspend fun deletar(amigo: Amigo)
}