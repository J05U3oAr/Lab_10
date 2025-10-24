package com.example.lab10.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab10.data.Location
import com.example.lab10.repo.LocationRepository
import com.example.lab10.ui.loadings.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class LocationDetailViewModel(
    private val repo: LocationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationId: Int = checkNotNull(savedStateHandle["id"])
    private val _state = MutableStateFlow(UiState<Location>(isLoading = true))
    val state = _state.asStateFlow()

    init { load() }

    private fun load() {
        _state.value = UiState(isLoading = true)
        viewModelScope.launch {
            try {
                delay(2000)

                val roll = Random.nextInt(1, 11)
                if (roll % 2 == 0) {
                    val loc = repo.getLocationById(locationId)  // ✅ Lee de Room
                    _state.value = UiState(isLoading = false, data = loc, hasError = false)
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