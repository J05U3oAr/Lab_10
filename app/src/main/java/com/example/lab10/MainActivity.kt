package com.example.lab10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.lab10.ui.login.DestinoLogin
import com.example.lab10.ui.menuHome.DestinoHome
import com.example.lab10.ui.login.loginGraph
import com.example.lab10.ui.menuHome.homeGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val nav = rememberNavController()

                    NavHost(
                        navController = nav,
                        startDestination = DestinoLogin
                    ) {
                        loginGraph(
                            onSuccess = {
                                nav.navigate(DestinoHome) {
                                    popUpTo(0)
                                }
                            }
                        )

                        homeGraph(
                            onCerrarSesion = {
                                nav.navigate(DestinoLogin) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
