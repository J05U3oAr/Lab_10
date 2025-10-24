package com.example.lab10.ui.location

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lab10.entity.DbProvider
import com.example.lab10.repo.LocationRepositoryRoom
import com.example.lab10.ui.loadings.ErrorCard
import com.example.lab10.ui.viewmodel.LocationDetailViewModel
// En screenCharacter.kt y screenCharacterDetail.kt
import com.example.lab10.network.RetrofitInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screenLocationsDetails(
    on_back: () -> Unit,
    vm: LocationDetailViewModel = run {
        val ctx = LocalContext.current
        viewModel(
            factory = viewModelFactory {
                initializer {
                    val db = DbProvider.get(ctx)
                    val api = RetrofitInstance.api  // ✅ AGREGADO
                    val repo = LocationRepositoryRoom(db.locationDao(), api)  // ✅ Ahora recibe 2 parámetros
                    LocationDetailViewModel(
                        repo = repo,
                        savedStateHandle = createSavedStateHandle()
                    )
                }
            }
        )
    }
) {
    // ... resto del código sin cambios
    val ui = vm.state.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Details") },
                navigationIcon = {
                    IconButton(onClick = on_back) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "volver", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9C27B0),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when {
            ui.isLoading -> Box(Modifier.padding(padding).fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF9C27B0))
            }
            ui.hasError || ui.data == null -> ErrorCard(
                message = "No se pudo obtener la informacion de la locacion",
                onRetry = { vm.retry() },
                modifier = Modifier.padding(padding)
            )
            else -> {
                val loc = ui.data
                Column(
                    modifier = Modifier.padding(padding).padding(24.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(loc.name, style = MaterialTheme.typography.titleLarge)
                    Info("ID", loc.id.toString())
                    Info("Type", loc.type)
                    Info("Dimension", loc.dimension)
                }
            }
        }
    }
}

@Composable
private fun Info(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}
