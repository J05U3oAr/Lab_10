package com.example.lab10.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lab10.repo.AuthRepository
import com.example.lab10.repo.AuthRepositoryImpl
import com.example.lab10.ui.PrefsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val isLoggedIn: StateFlow<Boolean> =
        repo.isLoggedIn.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val userName: StateFlow<String?> =
        repo.userName.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /** Login: guarda el nombre en DataStore */
    fun login(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repo.login(name)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }

    companion object {
        fun factory(ctx: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val prefs = PrefsDataStore(ctx)
                    val repo: AuthRepository = AuthRepositoryImpl(prefs)
                    return AuthViewModel(repo) as T
                }
            }
    }
}
