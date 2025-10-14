package com.example.lab10.ui.menuHome

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

//Serializable para la opcion de character en la navegacion.
@Serializable
data object DestinoTabCharacters

//Serializable para la opcion de locations en la navegacion
@Serializable
data object DestinoTabLocations

//Serializable para la opcion de profile en la navegacion.
@Serializable
data object DestinoTabProfile

//atributos de navegacion.
private data class ItemBottom(
    val label: String,
    val icon: ImageVector,
    val route: Any
)


//Funcion de la pantalla de home
@Composable
fun screenHome(
    cerrar_sesion: () -> Unit
) {
    val navBottom = rememberNavController()

    //Configuracion de navegacion de la barra inferior.
    val items = listOf(
        ItemBottom("Characters", Icons.Filled.Person,        DestinoTabCharacters),
        ItemBottom("Locations",  Icons.Filled.LocationOn,    DestinoTabLocations),
        ItemBottom("Profile",    Icons.Filled.AccountCircle, DestinoTabProfile),
    )

    //Barra de navegacion.
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.95f)) {
                val backEntry by navBottom.currentBackStackEntryAsState()
                val current = backEntry?.destination?.route
                items.forEach { item ->
                    val selected = current == item.route::class.qualifiedName
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navBottom.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(DestinoTabCharacters) { saveState = true }
                            }
                        },
                        icon  = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->

        //Navegacion inferior.
        BarraNavegacion(
            navBottom       = navBottom,
            paddingValues   = padding,
            cerrar_sesion = cerrar_sesion
        )
    }
}

//Funcion de la barra de navegacion.
@Composable
private fun BarraNavegacion(
    navBottom: NavHostController,
    paddingValues: PaddingValues,
    cerrar_sesion: () -> Unit
) {

    NavHost(
        navController = navBottom,
        startDestination = DestinoTabCharacters,
        modifier = Modifier.padding(paddingValues)
    ) {
        // Asignacion de destino a la pestania de personajes.
        navigation<DestinoTabCharacters>(startDestination = com.example.lab10.ui.characters.GraphCharacters) {
            com.example.lab10.ui.characters.RutasPersonajes(
                builder = this,
                nav = navBottom
            )
        }

        // Asignacion de destino a la pestania de locaciones
        navigation<DestinoTabLocations>(startDestination = com.example.lab10.ui.location.GraphLocations) {
            com.example.lab10.ui.location.RutasLocations(
                builder = this,
                nav = navBottom
            )
        }

        // Asignacion de destiono a la pestania de perfil.
        navigation<DestinoTabProfile>(startDestination = com.example.lab10.ui.perfil.DestinoProfile) {
            com.example.lab10.ui.perfil.RegistrarProfile(
                builder = this,
                cerrar_sesion = cerrar_sesion
            )
        }
    }
}