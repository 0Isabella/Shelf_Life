package com.example.shelflife.model

import androidx.annotation.DrawableRes

data class Amigo(
    val id: Int,
    val nome: String,
    val minutosLidos: Int,
    val emoji: String = "🙂",
    @DrawableRes val imagemRes: Int? = null,
    val livrosLidos: Int = 0,
    val sequenciaDias: Int = 0
)