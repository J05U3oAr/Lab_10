package com.example.lab10.ui.location

//Librerias importadas
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

//Navegacion de las locaciones

//Contenedor para agrupar la informacion de las localidades.
@Serializable data object GraphLocations

//Destino de la lista de localidades.
@Serializable data object DestinoLocationsList

//Destino de la informacon detallada de las locaciones.
//El id se serializa en ruta, donde el viewmodel lo lee.
@Serializable data class DestinoLocationDetail(val id: Int)

//Funcion de Registro de rutas de las locaciones.
fun RutasLocations(
    builder: NavGraphBuilder,
    nav: NavHostController
) {

    //Creacion del subgrafo de locaciones con la lista.
    builder.navigation<GraphLocations>(startDestination = DestinoLocationsList) {

        //Ruta tipada a la lista de locaciones
        composable<DestinoLocationsList> {
            //Llama a la funcion de pantalla de las locaciones.
            screenLocations(
                on_click_location = { id ->
                    nav.navigate(DestinoLocationDetail(id))
                }
            )
        }

        //Ruta tipada detallada de las locaciones
        composable<DestinoLocationDetail> {

            //Lectura del id desde el savedStateHandle del viewModel.
            screenLocationsDetails(
                on_back = { nav.popBackStack() }
            )
        }
    }
}