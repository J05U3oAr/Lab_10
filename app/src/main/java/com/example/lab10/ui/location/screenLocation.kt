package com.example.lab10.ui.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lab10.data.Location
import com.example.lab10.entity.DbProvider
import com.example.lab10.repo.LocationRepositoryRoom
import com.example.lab10.ui.loadings.ErrorCard
import com.example.lab10.ui.viewmodel.LocationsViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.lab10.network.RetrofitInstance


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screenLocations(
    on_click_location: (Int) -> Unit,
    vm: LocationsViewModel = run {
        val ctx = LocalContext.current
        viewModel(
            factory = viewModelFactory {
                initializer {
                    val db = DbProvider.get(ctx)
                    val api = RetrofitInstance.api  // ✅ AGREGADO
                    val repo = LocationRepositoryRoom(db.locationDao(), api)  // ✅ Ahora recibe 2 parámetros
                    LocationsViewModel(repo)
                }
            }
        )
    }
) {
    // ... resto del código sin cambios
    val uiState = vm.state.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9C27B0),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> Box(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Color(0xFF9C27B0)) }

            uiState.hasError -> ErrorCard(
                message = "No se pudo obtener la lista de locaciones",
                onRetry = { vm.loadLocations() },
                modifier = Modifier.padding(padding)
            )

            else -> {
                val locations = uiState.data.orEmpty()
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(locations, key = { it.id }) { loc ->
                        Locaciones(loc) { on_click_location(loc.id) }
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun Locaciones(
    loc: Location,
    on_click: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().clickable { on_click() }.padding(16.dp)
    ) {
        Text(loc.name, style = MaterialTheme.typography.titleMedium)
        Text(loc.type, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
