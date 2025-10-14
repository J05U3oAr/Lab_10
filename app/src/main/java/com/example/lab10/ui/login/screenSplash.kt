package com.example.lab10.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab10.ui.viewmodel.SplashViewModel

@Composable
fun screenSplash(
    onGoLogin: () -> Unit,
    onGoHome: () -> Unit,
    vm: SplashViewModel = viewModel(
        factory = SplashViewModel.provideFactory(LocalContext.current)
    )
) {
    val isLoggedIn by vm.isLoggedIn.collectAsStateWithLifecycle()

    LaunchedEffect(isLoggedIn) {
        when (isLoggedIn) {
            true  -> onGoHome()
            false -> onGoLogin()
            else  -> Unit
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
