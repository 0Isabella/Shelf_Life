package com.example.shelflife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelflife.data.LivrosIniciais
import com.example.shelflife.data.repository.LivroRepository
import com.example.shelflife.model.Livro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LivrosViewModel(private val repository: LivroRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LivrosUiState())
    val uiState: StateFlow<LivrosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (repository.buscarTodos().first().isEmpty()) {
                LivrosIniciais.forEach { repository.inserir(it.copy(id = 0)) }
            }
            repository.buscarTodos().collect { lista ->
                _uiState.update { it.copy(livros = lista, carregando = false) }
            }
        }
    }

    fun adicionarLivro(titulo: String, autor: String, paginas: Int) {
        viewModelScope.launch {
            repository.inserir(
                Livro(
                    titulo = titulo,
                    autor = autor.ifBlank { "Autor desconhecido" },
                    paginasTotal = paginas
                )
            )
        }
    }

    fun atualizarLivro(livro: Livro) {
        viewModelScope.launch { repository.atualizar(livro) }
    }

    fun removerLivro(livro: Livro) {
        viewModelScope.launch { repository.deletar(livro) }
    }
}
