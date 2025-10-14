package com.example.lab10.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lab10.data.Location
import com.example.lab10.entity.DbProvider
import com.example.lab10.repo.LocationRepository
import com.example.lab10.repo.LocationRepositoryRoom
import com.example.lab10.ui.loadings.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationsViewModel(
    private val repo: LocationRepository
) : ViewModel() {

    // Estado interno
    private val _state = MutableStateFlow(UiState<List<Location>>(isLoading = true))
    // Estado expuesto a la UI
    val state = _state.asStateFlow()

    init { loadLocations() }

    fun loadLocations() {
        _state.value = UiState(isLoading = true)
        viewModelScope.launch {
            try {

                delay(4000)

                val list = repo.getLocations()        // Lee desde Room
                _state.value = UiState(isLoading = false, data = list, hasError = false)
            } catch (_: Exception) {
                _state.value = UiState(isLoading = false, data = null, hasError = true)
            }
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = DbProvider.get(context)
                    val repo = LocationRepositoryRoom(db.locationDao())
                    return LocationsViewModel(repo) as T
                }
            }
    }
}
