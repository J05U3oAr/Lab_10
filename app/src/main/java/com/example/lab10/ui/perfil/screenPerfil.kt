package com.example.lab10.ui.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lab10.ui.viewmodel.ProfileViewModel

/** Wrapper que conecta el VM y llama a tu UI 'pantallaProfile' */
@Composable
fun screenProfile(
    onCerrarSesion: () -> Unit,
    vm: ProfileViewModel = viewModel(
        factory = ProfileViewModel.provideFactory(LocalContext.current)
    )
) {
    val name by vm.userName.collectAsStateWithLifecycle()

    pantallaProfile(
        nombre = name ?: "Invitado",
        carne = "241403",
        on_cerrar_sesion = {
            vm.logout()
            onCerrarSesion()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pantallaProfile(
    nombre: String,
    carne: String,
    on_cerrar_sesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF27F5F2),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AsyncImage(
                model = "https://i.pinimg.com/736x/fc/34/0e/fc340e09bdff93e0cea4c3c6cbe2516e.jpg",
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Nombre:", fontWeight = FontWeight.SemiBold)
                Text(nombre, fontSize = 18.sp)

                Spacer(Modifier.height(8.dp))

                Text("Carné:", fontWeight = FontWeight.SemiBold)
                Text(carne, fontSize = 18.sp)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = on_cerrar_sesion,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27F5F2),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
