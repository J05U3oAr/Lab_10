package com.example.lab10.ui.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lab10.entity.DbProvider
import com.example.lab10.repo.CharacterRepositoryRoom
import com.example.lab10.ui.viewmodel.CharactersViewModel
import com.example.lab10.data.Character
import com.example.lab10.ui.loadings.ErrorCard
import com.example.lab10.network.RetrofitInstance



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screenCharacters(
    on_click_personaje: (Int) -> Unit,
    vm: CharactersViewModel = run {
        val ctx = LocalContext.current
        viewModel(
            factory = viewModelFactory {
                initializer {
                    val db = DbProvider.get(ctx)
                    val api = RetrofitInstance.api  // ✅ AGREGADO
                    val repo = CharacterRepositoryRoom(db.characterDao(), api)  // ✅ Ahora recibe 2 parámetros
                    CharactersViewModel(repo)
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
                title = { Text("Characters") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9C27B0),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> Box(
                Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Color(0xFF9C27B0)) }

            uiState.hasError -> ErrorCard(
                message = "No se pudo obtener la lista de personajes",
                onRetry = { vm.loadCharacters() },
                modifier = Modifier.padding(padding)
            )

            else -> {
                val personajes = uiState.data.orEmpty()
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(personajes, key = { it.id }) { p ->
                        Personaje(p) { on_click_personaje(p.id) }
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun Personaje(
    personaje: Character,
    on_click: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { on_click() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!personaje.image.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(personaje.image)
                    .crossfade(true)
                    .build(),
                contentDescription = personaje.name,
                modifier = Modifier.size(48.dp).clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(personaje.name, style = MaterialTheme.typography.titleMedium)
            Text("${personaje.species} - ${personaje.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
