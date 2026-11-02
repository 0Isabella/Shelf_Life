package com.example.shelflife.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shelflife.model.Livro

@Dao
interface LivroDao {
    @Query("SELECT * FROM livros")
    suspend fun buscarTodos(): List<Livro>

    @Query("SELECT * FROM livros WHERE id = :id")
    suspend fun buscarPorId(id: Int): Livro?

    @Insert
    suspend fun inserir(livro: Livro): Long

    @Update
    suspend fun atualizar(livro: Livro)

    @Delete
    suspend fun deletar(livro: Livro)
}