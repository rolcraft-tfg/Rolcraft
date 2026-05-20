package com.example.rolcraft.Login

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.rolcraft.CrearPersonaje.PersonajeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaLogin(
    onLoginCorrecto: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    viewModel: PersonajeViewModel
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // Mostrar u ocultar contraseña

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    // Mensaje de error

    var mensajeError by remember {
        mutableStateOf("")
    }

    // Carga

    var cargando by remember {
        mutableStateOf(false)
    }

    // Firebase

    val auth = FirebaseAuth.getInstance()

    // Preferencias

    val context = LocalContext.current

    val prefs = context.getSharedPreferences(
        "login_prefs",
        Context.MODE_PRIVATE
    )

    // Recordar sesión

    var recordarSesion by remember {

        mutableStateOf(
            prefs.getBoolean(
                "recordar",
                false
            )
        )
    }

    // Cargar datos guardados

    LaunchedEffect(Unit) {

        if (recordarSesion) {

            usuario =
                prefs.getString(
                    "email",
                    ""
                ) ?: ""

            password =
                prefs.getString(
                    "password",
                    ""
                ) ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Inicio de sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(40.dp))


        OutlinedTextField(
            value = usuario,

            onValueChange = {
                usuario = it
            },

            label = {
                Text("Email")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
            },

            label = {
                Text("Contraseña")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

            trailingIcon = {

                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {

                    Icon(
                        imageVector =
                            if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,

                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Recordar sesión

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = recordarSesion,

                onCheckedChange = {
                    recordarSesion = it
                }
            )

            Text(
                text = "Recordar usuario y contraseña"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                mensajeError = ""

                // Validar campos vacíos

                if (
                    usuario.isBlank() ||
                    password.isBlank()
                ) {

                    mensajeError =
                        "Completa todos los campos"

                    return@Button
                }

                cargando = true

                auth.signInWithEmailAndPassword(
                    usuario.trim(),
                    password.trim()
                ).addOnCompleteListener { task ->

                    cargando = false

                    if (task.isSuccessful) {

                        val user = auth.currentUser

                        if (user != null && user.isEmailVerified) {

                            // Guardar contraseña y nombre

                            if (recordarSesion) {

                                prefs.edit()
                                    .putString("email", usuario.trim())
                                    .putString("password", password.trim())
                                    .putBoolean("recordar", true)
                                    .apply()

                            } else {

                                prefs.edit()
                                    .clear()
                                    .apply()
                            }

                            onLoginCorrecto()

                        } else {

                            mensajeError = "Debes verificar tu correo antes de iniciar sesión"
                            auth.signOut()
                        }

                    } else {

                        mensajeError = "Usuario o contraseña incorrectos"
                    }
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            if (cargando) {

                CircularProgressIndicator()

            } else {

                Text("Iniciar sesión")
            }
        }

        // Mostrar mensaje de error

        if (mensajeError.isNotEmpty()) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Recuperar contraseña

        TextButton(
            onClick = {
                onForgotPasswordClick()
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("¿Olvidaste tu contraseña?")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Registro

        TextButton(
            onClick = {
                onRegisterClick()
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("¿No tienes cuenta? Regístrate")
        }
    }
}