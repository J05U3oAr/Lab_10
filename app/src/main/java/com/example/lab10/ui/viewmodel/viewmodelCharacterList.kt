package com.example.lab10.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab10.data.Character
import com.example.lab10.repo.CharacterRepository
import com.example.lab10.ui.loadings.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class CharactersViewModel(
    private val repo: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState<List<Character>>(isLoading = true))
    val state = _state.asStateFlow()

    init { loadCharacters() }

    fun loadCharacters() {
        _state.value = UiState(isLoading = true)
        viewModelScope.launch {
            try {
                delay(2000)  // Simulación de delay (puedes quitarlo si quieres)

                val roll = Random.nextInt(1, 11)
                if (roll % 2 == 0) {
                    val list = repo.getCharacters()  // ✅ Ahora llama al API si no hay data local
                    _state.value = UiState(isLoading = false, data = list, hasError = false)
                } else {
                    _state.value = UiState(isLoading = false, data = null, hasError = true)
                }
            } catch (e: Exception) {
                _state.value = UiState(isLoading = false, data = null, hasError = true)
            }
        }
    }
}