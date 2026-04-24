package com.example.rolcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.rolcraft.CrearPersonaje.PersonajeViewModel
import com.example.rolcraft.CrearPersonaje.PersonajeViewModelFactory
import com.example.rolcraft.CrearPersonaje.PantallaCrearPersonaje
import com.example.rolcraft.CrearPersonaje.PantallaDatosPersonaje
import com.example.rolcraft.Data.Local.AppDatabase
import com.example.rolcraft.Data.Repository.PersonajeRepository
import com.example.rolcraft.Inicio.PantallaInicio
import com.example.rolcraft.ui.login.PantallaLogin
import com.example.rolcraft.ui.login.PantallaRecuperar
import com.example.rolcraft.ui.login.PantallaRegistro
import com.example.rolcraft.ui.theme.RolCraftTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: PersonajeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ Base de datos Room
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "rolcraft_db"
        ).build()

        // ⭐ Repository
        val repository = PersonajeRepository(db.personajeDao())

        // ⭐ ViewModel con Factory
        viewModel = ViewModelProvider(
            this,
            PersonajeViewModelFactory(repository)
        )[PersonajeViewModel::class.java]

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
        startDestination = "login"
    ) {

        // ⭐ LOGIN
        composable("login") {
            PantallaLogin(
                onLoginClick = { _, _ ->
                    navController.navigate("inicio")
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

        // ⭐ RECUPERAR
        composable("recuperar") {
            PantallaRecuperar(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // ⭐ INICIO (🔥 CAMBIO IMPORTANTE)
        composable("inicio") {
            PantallaInicio(
                viewModel = viewModel,
                onCrearPersonaje = {
                    navController.navigate("crear")
                },
                onDados = {
                    navController.navigate("dados")
                },
                onCampania = {
                    // futura pantalla
                },
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo("inicio") { inclusive = true }
                    }
                }
            )
        }

        // ⭐ CREAR PERSONAJE
        composable("crear") {
            PantallaCrearPersonaje(
                viewModel = viewModel,
                onSiguiente = {
                    navController.navigate("ficha")
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // ⭐ FICHA FINAL
        composable("ficha") {
            PantallaDatosPersonaje(
                viewModel = viewModel,
                onAnterior = {
                    navController.popBackStack()
                },
                onGuardar = {
                    viewModel.guardarPersonaje()

                    navController.navigate("inicio") {
                        popUpTo("inicio") { inclusive = true }
                    }
                },
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