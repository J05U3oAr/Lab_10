package com.example.lab10.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lab10.repo.AuthRepository
import com.example.lab10.repo.AuthRepositoryImpl
import com.example.lab10.ui.PrefsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    val userName: StateFlow<String?> =
        repo.userName.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun logout() {
        viewModelScope.launch { repo.logout() }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val prefs = PrefsDataStore(context)
                    val repo: AuthRepository = AuthRepositoryImpl(prefs)
                    return ProfileViewModel(repo) as T
                }
            }
    }
}
