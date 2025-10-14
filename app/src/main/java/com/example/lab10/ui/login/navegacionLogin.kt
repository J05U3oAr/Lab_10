package com.example.lab10.ui.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DestinoLogin

fun NavGraphBuilder.loginGraph(
    onSuccess: () -> Unit
) {
    composable<DestinoLogin> {
        Scaffold { padding ->
            screenLogin(
                onSuccess = onSuccess,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

