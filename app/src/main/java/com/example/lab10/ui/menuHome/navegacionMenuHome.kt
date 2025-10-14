package com.example.lab10.ui.menuHome

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DestinoHome

fun NavGraphBuilder.homeGraph(
    onCerrarSesion: () -> Unit
) {
    composable<DestinoHome> {
        screenHome(
            cerrar_sesion = onCerrarSesion
        )
    }
}
