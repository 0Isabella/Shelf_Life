package com.example.shelflife.viewmodel

import com.example.shelflife.model.Livro
data class LivrosUiState(
    val livros: List<Livro> = emptyList(),
    val carregando: Boolean = true
)
