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
import com.example.rolcraft.CrearPersonaje.PantallaCrearPersonaje
import com.example.rolcraft.CrearPersonaje.PersonajeViewModel
import com.example.rolcraft.Inicio.PantallaInicio
import com.example.rolcraft.ui.login.PantallaLogin
import com.example.rolcraft.ui.login.PantallaRecuperar
import com.example.rolcraft.ui.login.PantallaRegistro
import com.example.rolcraft.ui.theme.RolCraftTheme

class MainActivity : ComponentActivity() {

    // ⭐ ViewModel compartido entre todas las pantallas
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

    // ⭐ Controlador de navegación
    val navController = rememberNavController()

    NavHost(
        navController = navController,

        // ⭐ Pantalla inicial de la app
        startDestination = "login"
    ) {

        // ⭐ LOGIN
        composable("login") {
            PantallaLogin(

                // ⭐ Si inicia sesión, va a la pantalla principal
                onLoginClick = { _, _ ->
                    navController.navigate("inicio")
                },

                // ⭐ Ir a registro
                onRegisterClick = {
                    navController.navigate("registro")
                },

                // ⭐ Ir a recuperar contraseña
                onForgotPasswordClick = {
                    navController.navigate("recuperar")
                }
            )
        }

        // ⭐ REGISTRO
        composable("registro") {
            PantallaRegistro(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // ⭐ RECUPERAR CONTRASEÑA
        composable("recuperar") {
            PantallaRecuperar(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // ⭐ PANTALLA PRINCIPAL CON LOS PERSONAJES
        composable("inicio") {
            PantallaInicio(

                // ⭐ Lista de personajes guardados
                personajes = viewModel.personajesGuardados,

                // ⭐ Botón "Crear personaje"
                onCrearPersonaje = {
                    navController.navigate("crear")
                },

                // ⭐ Botón "Dados"
                onDados = {
                    // Aquí irá tu futura pantalla de dados
                },

                // ⭐ Botón "Mi campaña"
                onCampania = {
                    // Aquí irá tu futura pantalla de campaña
                },

                // ⭐ Botón "Cerrar sesión"
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo("inicio") { inclusive = true }
                    }
                }
            )
        }

        // ⭐ PANTALLA PARA CREAR PERSONAJE
        composable("crear") {
            PantallaCrearPersonaje(
                viewModel = viewModel,

                // ⭐ Ir a la ficha final
                onSiguiente = {
                    navController.navigate("ficha")
                },

                // ⭐ Volver a la pantalla principal
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // ⭐ FICHA FINAL DEL PERSONAJE
        composable("ficha") {
            PantallaFichaPersonaje(
                viewModel = viewModel,

                // ⭐ Volver a la pantalla de crear personaje
                onAnterior = {
                    navController.popBackStack()
                },

                // ⭐ Guardar personaje y volver a inicio
                onGuardar = {
                    viewModel.guardarPersonaje()

                    navController.navigate("inicio") {
                        popUpTo("inicio") { inclusive = true }
                    }
                },

                // ⭐ Crear otro personaje directamente
                onNuevoPersonaje = {
                    viewModel.resetearPersonaje()

                    navController.navigate("crear") {
                        popUpTo("crear") { inclusive = true }
                    }
                }
            )
        }
    }
}