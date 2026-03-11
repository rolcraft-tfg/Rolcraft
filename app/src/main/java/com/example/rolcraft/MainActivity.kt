package com.example.rolcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rolcraft.Personaje.PantallaCrearPersonaje1
import com.example.rolcraft.Personaje.PantallaCrearPersonaje2
import com.example.rolcraft.Personaje.PantallaFichaPersonaje
import com.example.rolcraft.Personaje.PersonajeViewModel
import com.example.rolcraft.ui.theme.RolCraftTheme

class MainActivity : ComponentActivity() {

    private val viewModel: PersonajeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RolCraftTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavegacion(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavegacion(viewModel: PersonajeViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "pantalla1"
    ) {

        // ⭐ PANTALLA 1
        composable("pantalla1") {
            PantallaCrearPersonaje1(
                viewModel = viewModel,
                onSiguiente = {
                    navController.navigate("pantalla2")
                }
            )
        }

        // ⭐ PANTALLA 2
        composable("pantalla2") {
            PantallaCrearPersonaje2(
                viewModel = viewModel,
                onAnterior = {
                    navController.popBackStack()
                },
                onSiguiente = {
                    navController.navigate("ficha")
                }
            )
        }

        // ⭐ FICHA FINAL
        composable("ficha") {
            PantallaFichaPersonaje(
                viewModel = viewModel,
                onAnterior = {
                    navController.popBackStack()
                },
                onGuardar = {
                    viewModel.guardarPersonaje()
                },
                onNuevoPersonaje = {

                    viewModel.resetearPersonaje()

                    navController.navigate("pantalla1") {
                        popUpTo("pantalla1") { inclusive = true }
                    }
                }
            )
        }
    }
}