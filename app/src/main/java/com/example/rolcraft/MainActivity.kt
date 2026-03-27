package com.example.rolcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rolcraft.Personaje.PantallaCrearPersonaje1
import com.example.rolcraft.Personaje.PantallaCrearPersonaje2
import com.example.rolcraft.Personaje.PantallaFichaPersonaje
import com.example.rolcraft.Personaje.PersonajeViewModel
import com.example.rolcraft.ui.login.PantallaLogin
import com.example.rolcraft.ui.login.PantallaRegistro
import com.example.rolcraft.ui.login.PantallaRecuperar
import com.example.rolcraft.ui.theme.RolCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val nav = rememberNavController()

            NavHost(navController = nav, startDestination = "ficha") {

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

                    // ⭐ FORMA CORRECTA
                    navController.navigate("pantalla1") {
                        popUpTo("pantalla1") { inclusive = true }
                    }
                }
            )
        }
    }
}
