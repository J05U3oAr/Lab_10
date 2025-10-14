package com.example.lab10.ui.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lab10.entity.DbProvider
import com.example.lab10.repo.CharacterRepositoryRoom
import com.example.lab10.ui.loadings.ErrorCard
import com.example.lab10.ui.viewmodel.CharacterDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screenCharacterDetail(
    on_back: () -> Unit,
    vm: CharacterDetailViewModel = run {
        val ctx = LocalContext.current
        viewModel(
            factory = viewModelFactory {
                initializer {
                    val db = DbProvider.get(ctx)
                    val repo = CharacterRepositoryRoom(db.characterDao())
                    CharacterDetailViewModel(
                        repo = repo,
                        savedStateHandle = createSavedStateHandle()
                    )
                }
            }
        )
    }
) {
    val ui = vm.state.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Detail") },
                navigationIcon = {
                    IconButton(onClick = on_back) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF27F5F2),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when {
            ui.isLoading -> Box(Modifier.padding(padding).fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF27F5F2))
            }
            ui.hasError || ui.data == null -> ErrorCard(
                message = "No se pudo obtener la informacion de los personajes",
                onRetry = { vm.retry() },
                modifier = Modifier.padding(padding)
            )
            else -> {
                val c = ui.data
                Column(
                    modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (!c.image.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(c.image).crossfade(true).build(),
                            contentDescription = c.name,
                            modifier = Modifier.size(140.dp).clip(CircleShape)
                        )
                    } else {
                        Box(Modifier.size(140.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer))
                    }
                    Text(c.name, style = MaterialTheme.typography.titleLarge)
                    Info("Species", c.species); Info("Status", c.status); Info("Gender", c.gender)
                }
            }
        }
    }
}

@Composable
private fun Info(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}
