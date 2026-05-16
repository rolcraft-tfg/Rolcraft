package com.example.rolcraft

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.room.Room
import com.example.rolcraft.Ajustes.PantallaAjustes
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
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.edit

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: PersonajeViewModel

    override fun dispatchGenericMotionEvent(
        ev: MotionEvent?
    ): Boolean {

        if (
            ev?.action == MotionEvent.ACTION_HOVER_ENTER ||
            ev?.action == MotionEvent.ACTION_HOVER_MOVE ||
            ev?.action == MotionEvent.ACTION_HOVER_EXIT
        ) {
            return true
        }

        return super.dispatchGenericMotionEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "rolcraft_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository =
            PersonajeRepository(
                db.personajeDao()
            )

        viewModel = ViewModelProvider(
            this,
            PersonajeViewModelFactory(repository)
        )[PersonajeViewModel::class.java]

        val prefs = getSharedPreferences(
            "settings",
            MODE_PRIVATE
        )

        val auth =
            FirebaseAuth.getInstance()

        setContent {

            var modoOscuro by remember {
                mutableStateOf(
                    prefs.getBoolean(
                        "modo_oscuro",
                        true
                    )
                )
            }

            RolCraftTheme(
                darkTheme = modoOscuro
            ) {

                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.pointerInput(Unit) {}
                ) {

                    AppNavegacion(

                        viewModel = viewModel,

                        modoOscuro = modoOscuro,

                        onCambiarTema = {

                            modoOscuro = it

                            prefs.edit {
                                putBoolean(
                                    "modo_oscuro",
                                    it
                                )
                            }
                        },

                        auth = auth
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavegacion(

    viewModel: PersonajeViewModel,

    modoOscuro: Boolean,

    onCambiarTema: (Boolean) -> Unit,

    auth: FirebaseAuth
) {

    val navController =
        rememberNavController()

    Scaffold(

        modifier = Modifier
            .pointerInput(Unit) {},

        bottomBar = {

            val rutaActual =
                navController
                    .currentBackStackEntryAsState()
                    .value
                    ?.destination
                    ?.route

            if (
                rutaActual != "login" &&
                rutaActual != "registro" &&
                rutaActual != "recuperar"
            ) {

                BarraInferior(
                    navController = navController,
                    rutaActual = rutaActual,
                    auth = auth
                )
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

                    onLoginCorrecto = {

                        navController.navigate("inicio") {

                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    },

                    onRegisterClick = {

                        navController.navigate("registro")
                    },

                    onForgotPasswordClick = {

                        navController.navigate("recuperar")
                    },

                    // =========================
                    // INVITADO
                    // =========================

                    onGuestLogin = {

                        navController.navigate("inicio") {

                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // REGISTRO

            composable("registro") {

                PantallaRegistro(

                    onVolver = {

                        navController.popBackStack()
                    }
                )
            }

            // RECUPERAR

            composable("recuperar") {

                PantallaRecuperar(

                    onVolver = {

                        navController.popBackStack()
                    }
                )
            }

            // INICIO

            composable("inicio") {

                PantallaInicio(

                    viewModel = viewModel,

                    onCrearPersonaje = {
                        viewModel.resetearPersonaje()

                        navController.navigate("crear")
                    },

                    onPantallaFichaPersonaje = { id ->

                        navController.navigate("ficha/$id")
                    }
                )
            }

            // HABILIDADES

            composable("habilidades") {
                PantallaHabilidades()
            }

            composable("crear") {

                LaunchedEffect(Unit) {

                    if (!viewModel.modoEdicion) {

                        viewModel.resetearPersonaje()
                    }
                }

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
                    onGuardar = {

                        viewModel.guardarPersonaje()

                        navController.navigate("inicio") {

                            popUpTo("inicio") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // FICHA

            composable("ficha/{id}") { backStackEntry ->

                val id =
                    backStackEntry.arguments
                        ?.getString("id")!!
                        .toInt()

                PantallaFichaPersonaje(
                    id = id,
                    viewModel = viewModel
                )
            }

            // AJUSTES

            composable("ajustes") {

                PantallaAjustes(

                    modoOscuro = modoOscuro,

                    onCambiarTema = onCambiarTema,

                    onCerrarSesion = {

                        navController.navigate("login") {

                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BarraInferior(

    navController: NavController,

    rutaActual: String?,

    auth: FirebaseAuth
) {

    NavigationBar {

        NavigationBarItem(

            selected = rutaActual == "inicio",

            onClick = {

                navController.navigate("inicio") {

                    popUpTo("inicio")

                    launchSingleTop = true
                }
            },

            icon = {
                Icon(Icons.Default.People, null)
            },

            label = {
                Text("Personajes")
            }
        )

        NavigationBarItem(

            selected = rutaActual == "habilidades",

            onClick = {

                navController.navigate("habilidades") {

                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, null) },
            label = { Text("Habilidades") }
        )

        NavigationBarItem(

            selected = rutaActual == "ajustes",

            onClick = {

                navController.navigate("ajustes") {

                    launchSingleTop = true
                }
            },

            icon = {
                Icon(Icons.Default.Settings, null)
            },

            label = {
                Text("Ajustes")
            }
        )

        NavigationBarItem(

            selected = false,

            onClick = {

                auth.signOut()

                navController.navigate("login") {

                    popUpTo("inicio") {
                        inclusive = true
                    }
                }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) },
            label = { Text("Salir") }
        )
    }
}