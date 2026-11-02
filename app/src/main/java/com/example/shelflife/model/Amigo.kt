package com.example.shelflife.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "amigos")
data class Amigo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val minutosLidos: Int,
    val emoji: String = "🙂",
    @DrawableRes val imagemRes: Int? = null,
    val livrosLidos: Int = 0,
    val sequenciaDias: Int = 0,
    val nota: String = ""
)