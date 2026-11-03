package com.example.shelflife.viewmodel

import com.example.shelflife.model.Amigo
data class AmigosUiState(
    val amigos: List<Amigo> = emptyList(),
    val carregando: Boolean = true
)
