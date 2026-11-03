package com.example.shelflife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shelflife.data.AmigosIniciais
import com.example.shelflife.data.repository.AmigoRepository
import com.example.shelflife.model.Amigo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AmigosViewModel(private val repository: AmigoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AmigosUiState())
    val uiState: StateFlow<AmigosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (repository.buscarTodos().first().isEmpty()) {
                AmigosIniciais.forEach { repository.inserir(it.copy(id = 0)) }
            }
            repository.buscarTodos().collect { lista ->
                _uiState.update { it.copy(amigos = lista, carregando = false) }
            }
        }
    }

    fun adicionarAmigo(amigo: Amigo) {
        if (_uiState.value.amigos.none { it.nome == amigo.nome }) {
            viewModelScope.launch { repository.inserir(amigo) }
        }
    }

    fun atualizarAmigo(amigo: Amigo) {
        viewModelScope.launch { repository.atualizar(amigo) }
    }

    fun removerAmigo(amigo: Amigo) {
        viewModelScope.launch { repository.deletar(amigo) }
    }
}
