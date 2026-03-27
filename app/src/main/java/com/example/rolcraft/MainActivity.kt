package com.example.rolcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rolcraft.crearPersonaje.PantallaCrearPersonaje1
import com.example.rolcraft.crearPersonaje.PantallaCrearPersonaje2
import com.example.rolcraft.crearPersonaje.PantallaCrearPersonaje3
import com.example.rolcraft.ui.login.PantallaLogin
import com.example.rolcraft.ui.login.PantallaRegistro
import com.example.rolcraft.ui.login.PantallaRecuperar
import com.example.rolcraft.crearPersonaje.PersonajeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "login"
            ) {

                // ⭐ LOGIN
                composable("login") {
                    PantallaLogin(
                        onLoginClick = { _, _ ->
                            navController.navigate("pantalla1")
                        },
                        onRegisterClick = {
                            navController.navigate("registro")
                        },
                        onForgotPasswordClick = {
                            navController.navigate("recuperar")
                        }
                    )
                }

                // ⭐ REGISTRO
                composable("registro") {
                    PantallaRegistro(
                        onVolver = { navController.popBackStack() }
                    )
                }

                // ⭐ RECUPERAR CONTRASEÑA
                composable("recuperar") {
                    PantallaRecuperar(
                        onVolver = { navController.popBackStack() }
                    )
                }

                // ⭐ PANTALLA CREAR PERSONAJE 1
                composable("pantalla1") {
                    val viewModel: PersonajeViewModel = viewModel()
                    PantallaCrearPersonaje1(
                        viewModel = viewModel,
                        onSiguiente = { navController.navigate("pantalla2") }
                    )
                }

                // ⭐ PANTALLA CREAR PERSONAJE 2
                composable("pantalla2") {
                    val viewModel: PersonajeViewModel = viewModel()
                    PantallaCrearPersonaje2(
                        viewModel = viewModel,
                        onAnterior = { navController.popBackStack() },
                        onSiguiente = { navController.navigate("pantalla3") }
                    )
                }

                // ⭐ PANTALLA CREAR PERSONAJE 3 (FICHA FINAL)
                composable("pantalla3") {
                    val viewModel: PersonajeViewModel = viewModel()
                    PantallaCrearPersonaje3(
                        viewModel = viewModel,
                        onAnterior = { navController.popBackStack() },
                        onGuardar = { viewModel.guardarPersonaje() },
                        onNuevoPersonaje = {
                            viewModel.resetearPersonaje()
                            navController.navigate("pantalla1") {
                                popUpTo("pantalla1") { inclusive = true }
                            }
                        }
                    )
                }

                // ⭐ FICHA PERSONAJE
                composable("ficha") {
                    val viewModel: PersonajeViewModel = viewModel()
                    PantallaFichaPersonaje(
                        viewModel = viewModel,
                        onAnterior = { navController.popBackStack() },
                        onGuardar = { viewModel.guardarPersonaje() },
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
    }
}
