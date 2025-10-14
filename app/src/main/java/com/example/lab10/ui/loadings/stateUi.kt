package com.example.lab10.ui.loadings

//Clase de estados.
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val hasError: Boolean = false
)