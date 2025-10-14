package com.example.lab10.ui.characters

//Bibliotecas importadas
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

//Asigna un contenedor de destino para agrupar la informacion de los personajes.
@Serializable data object GraphCharacters

//Destino de la lista de personajes
@Serializable data object DestinoCharactersList

//se aplica en la ruta. En el viewModel de detalle se lee como savedStateHandle.
@Serializable data class DestinoCharacterDetail(val id: Int)

//Funcion de registro de rutas del subgrado de los personajes.
fun RutasPersonajes(
    builder: NavGraphBuilder,
    nav: NavHostController
) {

    //Creacion de subgrafos.
    builder.navigation<GraphCharacters>(startDestination = DestinoCharactersList) {

        //Ruta tipada de la lista de personajes
        composable<DestinoCharactersList> {
            screenCharacters(
                on_click_personaje = { id ->
                    //Navegacion tipada, serializa id en la ruta.
                    nav.navigate(DestinoCharacterDetail(id))
                }
            )
        }

        //Ruta tipada detallada con id
        composable<DestinoCharacterDetail> {
            //Lectura para el viewModel del SavedStateHandle en lugar del id al composable.
            screenCharacterDetail(
                on_back = { nav.popBackStack() }
            )
        }
    }
}