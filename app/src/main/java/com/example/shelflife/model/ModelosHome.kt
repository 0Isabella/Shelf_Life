package com.example.shelflife.model

import androidx.annotation.DrawableRes

data class LivroHome(val id: Int, val titulo: String, @DrawableRes val imagemRes: Int? = null)
data class AmigoHome(val id: Int, val nome: String, @DrawableRes val imagemRes: Int? = null, val emoji: String = "🙂")