package com.example.lab10.ui.perfil

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DestinoProfile

fun RegistrarProfile(
    builder: NavGraphBuilder,
    cerrar_sesion: () -> Unit
) {
    builder.composable<DestinoProfile> {
        screenProfile(onCerrarSesion = cerrar_sesion)
    }
}
