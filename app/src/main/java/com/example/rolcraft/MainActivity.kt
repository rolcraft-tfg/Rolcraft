package com.example.rolcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.room.Room
import com.example.rolcraft.CrearPersonaje.*
import com.example.rolcraft.Data.Local.AppDatabase
import com.example.rolcraft.Data.Repository.PersonajeRepository
import com.example.rolcraft.FichaPersonaje.PantallaFichaPersonaje
import com.example.rolcraft.Inicio.PantallaInicio
import com.example.rolcraft.Listado.PantallaHabilidades
import com.example.rolcraft.Login.PantallaLogin
import com.example.rolcraft.RecuperarContrasenya.PantallaRecuperar
import com.example.rolcraft.Registro.PantallaRegistro
import com.example.rolcraft.ui.theme.RolCraftTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: PersonajeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Base de datos Room
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "rolcraft_db"
        ).build()

        // Repository
        val repository = PersonajeRepository(db.personajeDao())

        // ViewModel
        viewModel = ViewModelProvider(
            this,
            PersonajeViewModelFactory(repository)
        )[PersonajeViewModel::class.java]

        setContent {
            RolCraftTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavegacion(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavegacion(viewModel: PersonajeViewModel) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val rutaActual =
                navController.currentBackStackEntryAsState().value?.destination?.route

            if (rutaActual != "login" &&
                rutaActual != "registro" &&
                rutaActual != "recuperar"
            ) {
                BarraInferior(navController, rutaActual)
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {

            // LOGIN
            composable("login") {
                PantallaLogin(
                    onLoginClick = { _, _ -> navController.navigate("inicio") },
                    onRegisterClick = { navController.navigate("registro") },
                    onForgotPasswordClick = { navController.navigate("recuperar") }
                )
            }

            // REGISTRO
            composable("registro") {
                PantallaRegistro(
                    onVolver = { navController.popBackStack() }
                )
            }

            // RECUPERAR
            composable("recuperar") {
                PantallaRecuperar(
                    onVolver = { navController.popBackStack() }
                )
            }

            // INICIO (PERSONAJES)
            composable("inicio") {
                PantallaInicio(
                    viewModel = viewModel,
                    onCrearPersonaje = {
                        navController.navigate("crear")
                    },
                    onPantallaFichaPersonaje = {
                        navController.navigate("ficha")
                    },
                    onCerrarSesion = {
                        navController.navigate("login") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    }
                )
            }

            // HABILIDADES
            composable("habilidades") {
                PantallaHabilidades(
                    onVolver = { navController.popBackStack() }
                )
            }

            // CREAR PERSONAJE
            composable("crear") {
                PantallaCrearPersonaje(
                    viewModel = viewModel,
                    onSiguiente = {
                        navController.navigate("verFicha")
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }

            // VER FICHA
            composable("verFicha") {
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
                        navController.navigate("crear")
                    }
                )
            }

            // FICHA FINAL
            composable("ficha") {
                PantallaFichaPersonaje(
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
                        navController.navigate("crear")
                    }
                )
            }
        }
    }
}

@Composable
fun BarraInferior(navController: NavController, rutaActual: String?) {

    NavigationBar {

        NavigationBarItem(
            selected = rutaActual == "inicio",
            onClick = {
                navController.navigate("inicio") {
                    popUpTo("inicio")
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.ListAlt, null) },
            label = { Text("Personajes") }
        )

        NavigationBarItem(
            selected = rutaActual == "habilidades",
            onClick = {
                navController.navigate("habilidades") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.ListAlt, null) },
            label = { Text("Habilidades") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Ajustes") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("login") {
                    popUpTo("inicio") { inclusive = true }
                }
            },
            icon = { Icon(Icons.Default.ExitToApp, null) },
            label = { Text("Salir") }
        )
    }
}