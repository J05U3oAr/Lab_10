package com.example.lab10.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
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

class CharacterDetailViewModel(
    private val repo: CharacterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["id"])
    private val _state = MutableStateFlow(UiState<Character>(isLoading = true))
    val state = _state.asStateFlow()

    init { load() }

    private fun load() {
        _state.value = UiState(isLoading = true)
        viewModelScope.launch {
            try {
                delay(2000)

                val roll = Random.nextInt(1, 11)
                if (roll % 2 == 0) {
                    val character = repo.getCharacterById(characterId)  // ✅ Lee de Room
                    _state.value = UiState(isLoading = false, data = character, hasError = false)
                } else {
                    _state.value = UiState(isLoading = false, data = null, hasError = true)
                }
            } catch (_: Exception) {
                _state.value = UiState(isLoading = false, data = null, hasError = true)
            }
        }
    }

    fun retry() = load()
}