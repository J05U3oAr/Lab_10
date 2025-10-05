package com.example.lab_10

// Librerías importadas
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random


// ------------------ RUTAS SERIALIZABLES ------------------
@Serializable
object CharactersGraph

@Serializable
object LocationsGraph

@Serializable
object ProfileRoute

@Serializable
object CharacterListRoute

@Serializable
data class CharacterDetailRoute(val characterId: Int)

@Serializable
object LocationListRoute

@Serializable
data class LocationDetailRoute(val locationId: Int)


// ------------------ DATA CLASSES ------------------
@Serializable
data class Character(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val gender: String,
    val image: String
)

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

// ------------------ REPOSITORY ------------------
class CharacterRepository {
    private val characters = listOf(
        Character(1, "Rick Sanchez", "Human", "Alive", "Male", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
        Character(2, "Morty Smith", "Human", "Alive", "Male", "https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
        Character(3, "Summer Smith", "Human", "Alive", "Female", "https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
        Character(4, "Beth Smith", "Human", "Alive", "Female", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
        Character(5, "Jerry Smith", "Human", "Alive", "Male", "https://rickandmortyapi.com/api/character/avatar/5.jpeg")
    )

    suspend fun getAllCharacters(): List<Character> {
        delay(4000) // Simula llamada de red
        if (Random.nextInt(1, 11) % 2 != 0) throw Exception("Error al cargar personajes")
        return characters
    }

    suspend fun getCharacterById(id: Int): Character {
        delay(2000) // Simula llamada de red
        if (Random.nextInt(1, 11) % 2 != 0) throw Exception("Error al cargar personaje")
        return characters.first { it.id == id }
    }
}

class LocationRepository {
    private val locations = listOf(
        Location(1, "Earth (C-137)", "Planet", "Dimension C-137"),
        Location(2, "Abadango", "Cluster", "unknown"),
        Location(3, "Citadel of Ricks", "Space station", "unknown"),
        Location(4, "Worldender's lair", "Planet", "unknown"),
        Location(5, "Anatomy Park", "Microverse", "Dimension C-137"),
        Location(6, "Interdimensional Cable", "TV", "unknown"),
        Location(7, "Immortality Field Resort", "Resort", "unknown"),
        Location(8, "Post-Apocalyptic Earth", "Planet", "Post-Apocalyptic Dimension"),
        Location(9, "Purge Planet", "Planet", "Replacement Dimension"),
        Location(10, "Venzenulon 7", "Planet", "unknown"),
        Location(11, "Bepis 9", "Planet", "unknown"),
        Location(12, "Cronenberg Earth", "Planet", "Cronenberg Dimension"),
        Location(13, "Nuptia 4", "Planet", "unknown"),
        Location(14, "Giant's Town", "Fantasy town", "Fantasy Dimension"),
        Location(15, "Bird World", "Planet", "unknown"),
        Location(16, "St. Gloopy Noops Hospital", "Space station", "unknown"),
        Location(17, "Earth (5-126)", "Planet", "Dimension 5-126"),
        Location(18, "Mr. Goldenfold's dream", "Dream", "Dimension C-137"),
        Location(19, "Gromflom Prime", "Planet", "Replacement Dimension"),
        Location(20, "Earth (Replacement Dimension)", "Planet", "Replacement Dimension")
    )

    suspend fun getAllLocations(): List<Location> {
        delay(4000) // Simula llamada de red
        if (Random.nextInt(1, 11) % 2 != 0) throw Exception("Error al cargar ubicaciones")
        return locations
    }

    suspend fun getLocationById(id: Int): Location {
        delay(2000) // Simula llamada de red
        if (Random.nextInt(1, 11) % 2 != 0) throw Exception("Error al cargar ubicación")
        return locations.first { it.id == id }
    }
}

// ------------------ UI STATES ------------------
data class CharacterListState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

data class CharacterDetailState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

data class LocationListState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

data class LocationDetailState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

// ------------------ VIEWMODELS ------------------
class CharacterListViewModel : ViewModel() {
    private val repository = CharacterRepository()
    private val _state = MutableStateFlow(CharacterListState())
    val state: StateFlow<CharacterListState> = _state.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _state.value = CharacterListState(isLoading = true)
            try {
                val characters = repository.getAllCharacters()
                _state.value = CharacterListState(isLoading = false, data = characters)
            } catch (e: Exception) {
                _state.value = CharacterListState(isLoading = false, hasError = true)
            }
        }
    }
}

class CharacterDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val repository = CharacterRepository()
    private val _state = MutableStateFlow(CharacterDetailState())
    val state: StateFlow<CharacterDetailState> = _state.asStateFlow()

    private val characterId: Int = savedStateHandle.get<Int>("characterId") ?: 0

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch {
            _state.value = CharacterDetailState(isLoading = true)
            try {
                val character = repository.getCharacterById(characterId)
                _state.value = CharacterDetailState(isLoading = false, data = character)
            } catch (e: Exception) {
                _state.value = CharacterDetailState(isLoading = false, hasError = true)
            }
        }
    }
}

class LocationListViewModel : ViewModel() {
    private val repository = LocationRepository()
    private val _state = MutableStateFlow(LocationListState())
    val state: StateFlow<LocationListState> = _state.asStateFlow()

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            _state.value = LocationListState(isLoading = true)
            try {
                val locations = repository.getAllLocations()
                _state.value = LocationListState(isLoading = false, data = locations)
            } catch (e: Exception) {
                _state.value = LocationListState(isLoading = false, hasError = true)
            }
        }
    }
}

class LocationDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val repository = LocationRepository()
    private val _state = MutableStateFlow(LocationDetailState())
    val state: StateFlow<LocationDetailState> = _state.asStateFlow()

    private val locationId: Int = savedStateHandle.get<Int>("locationId") ?: 0

    init {
        loadLocation()
    }

    fun loadLocation() {
        viewModelScope.launch {
            _state.value = LocationDetailState(isLoading = true)
            try {
                val location = repository.getLocationById(locationId)
                _state.value = LocationDetailState(isLoading = false, data = location)
            } catch (e: Exception) {
                _state.value = LocationDetailState(isLoading = false, hasError = true)
            }
        }
    }
}

// ------------------ MAIN ACTIVITY ------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Lab10App() }
    }
}

// ------------------ BOTTOM NAVIGATION ------------------
@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        NavigationBarItem(
            label = { Text("Characters") },
            selected = currentDestination?.route?.contains("CharactersGraph") == true,
            onClick = {
                navController.navigate(CharactersGraph) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = null) }
        )

        NavigationBarItem(
            label = { Text("Locations") },
            selected = currentDestination?.route?.contains("LocationsGraph") == true,
            onClick = {
                navController.navigate(LocationsGraph) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
        )

        NavigationBarItem(
            label = { Text("Profile") },
            selected = currentDestination?.route?.contains("ProfileRoute") == true,
            onClick = {
                navController.navigate(ProfileRoute) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) }
        )
    }
}

// ------------------ APP NAVIGATION ------------------
@Composable
fun Lab10App() {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavBar(navController) }) { padding ->
        NavHost(
            navController = navController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(padding)
        ) {
            // Characters Graph
            navigation<CharactersGraph>(startDestination = CharacterListRoute) {
                composable<CharacterListRoute> {
                    CharactersScreen(navController)
                }
                composable<CharacterDetailRoute> {
                    CharacterDetailScreen(navController)
                }
            }

            // Locations Graph
            navigation<LocationsGraph>(startDestination = LocationListRoute) {
                composable<LocationListRoute> {
                    LocationsScreen(navController)
                }
                composable<LocationDetailRoute> {
                    LocationDetailScreen(navController)
                }
            }

            // Profile
            composable<ProfileRoute> {
                ProfileScreen(navController)
            }
        }
    }
}

// ------------------ LOADING & ERROR COMPOSABLES ------------------
@Composable
fun LoadingLayout() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text("Cargando...", fontSize = 18.sp)
    }
}

@Composable
fun ErrorLayout(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "¡Ups! Ocurrió un error",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
        Spacer(Modifier.height(8.dp))
        Text("No se pudo cargar la información")
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

// ------------------ CHARACTERS SCREENS ------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    navController: NavHostController,
    viewModel: CharacterListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column {
        CenterAlignedTopAppBar(
            title = { Text("Characters") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        when {
            state.isLoading -> LoadingLayout()
            state.hasError -> ErrorLayout(onRetry = { viewModel.loadCharacters() })
            else -> {
                LazyColumn {
                    items(state.data) { c ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(CharacterDetailRoute(c.id))
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = c.image,
                                contentDescription = null,
                                modifier = Modifier.size(52.dp).clip(CircleShape)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(c.name, fontWeight = FontWeight.Bold)
                                Text("${c.species} · ${c.status}", color = Color.Gray)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    navController: NavHostController,
    viewModel: CharacterDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Character Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when {
                state.isLoading -> LoadingLayout()
                state.hasError -> ErrorLayout(onRetry = { viewModel.loadCharacter() })
                else -> state.data?.let { character ->
                    Column(
                        Modifier.padding(16.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = character.image,
                            contentDescription = null,
                            modifier = Modifier.size(140.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(character.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Text("Status: ${character.status}")
                        Text("Species: ${character.species}")
                        Text("Gender: ${character.gender}")
                    }
                }
            }
        }
    }
}

// ------------------ LOCATIONS SCREENS ------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    navController: NavHostController,
    viewModel: LocationListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column {
        CenterAlignedTopAppBar(
            title = { Text("Locations") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        when {
            state.isLoading -> LoadingLayout()
            state.hasError -> ErrorLayout(onRetry = { viewModel.loadLocations() })
            else -> {
                LazyColumn {
                    items(state.data) { loc ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(LocationDetailRoute(loc.id))
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(loc.name, fontWeight = FontWeight.Bold)
                                Text("Tipo: ${loc.type}", color = Color.Gray)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    navController: NavHostController,
    viewModel: LocationDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Location Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when {
                state.isLoading -> LoadingLayout()
                state.hasError -> ErrorLayout(onRetry = { viewModel.loadLocation() })
                else -> state.data?.let { location ->
                    Column(Modifier.padding(16.dp)) {
                        Text(location.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Text("ID: ${location.id}")
                        Text("Type: ${location.type}")
                        Text("Dimension: ${location.dimension}")
                    }
                }
            }
        }
    }
}

// ------------------ PROFILE SCREEN ------------------
@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(120.dp))
            Spacer(Modifier.height(16.dp))
            Text("Nombre: Arodi Josué Chávez Ramírez")
            Text("Carné: 241112")
        }
    }
}