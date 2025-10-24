package com.example.lab10.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lab10.ui.viewmodel.LoginViewModel

@Composable
fun screenLogin(
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    val vm: LoginViewModel = viewModel(
        factory = LoginViewModel.provideFactory(ctx)
    )

    val isLoggedIn by vm.isLoggedIn.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onSuccess()
    }

    var name by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AsyncImage(
                model = "https://vectorseek.com/wp-content/uploads/2023/07/Rick-and-Morty-Logo-Vector.jpg",
                contentDescription = null,
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tu nombre") },
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Button(
                onClick = { if (name.isNotBlank()) vm.login(name) },
                enabled = !isLoading && name.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar sesión", fontSize = 16.sp)
                }
            }
        }

        Text(
            text = "Arodi Josué Chávez Ramírez — 241112",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge
        )
    }
}