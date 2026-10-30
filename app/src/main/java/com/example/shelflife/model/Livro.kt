package com.example.shelflife.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "livros")
data class Livro(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val autor: String = "Autor desconhecido",
    @DrawableRes val imagemRes: Int? = null,
    val paginasTotal: Int = 0,
    val paginasLidas: Int = 0,
    val minutosLidos: Int = 0,
    val sinopse: String = "",
    val anotacao: String = ""
)