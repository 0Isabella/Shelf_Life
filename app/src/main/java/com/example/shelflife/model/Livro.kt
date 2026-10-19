package com.example.shelflife.model

import androidx.annotation.DrawableRes

data class Livro(
    val id: Int,
    val titulo: String,
    val autor: String = "Autor desconhecido",
    @DrawableRes val imagemRes: Int? = null,
    val paginasTotal: Int = 0,
    val paginasLidas: Int = 0,
    val minutosLidos: Int = 0,
    val sinopse: String = "",
    val anotacao: String = ""
)