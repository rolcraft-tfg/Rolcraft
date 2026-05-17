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
    onGuestLogin: () -> Unit,
    viewModel: PersonajeViewModel
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    //mostrar u ocultar contraseña

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    //mensaje de error

    var mensajeError by remember {
        mutableStateOf("")
    }

    //carga

    var cargando by remember {
        mutableStateOf(false)
    }

    //firebase

    val auth = FirebaseAuth.getInstance()

    //shared preferences

    val context = LocalContext.current

    val prefs = context.getSharedPreferences(
        "login_prefs",
        Context.MODE_PRIVATE
    )

    //recordar sesión

    var recordarSesion by remember {

        mutableStateOf(
            prefs.getBoolean(
                "recordar",
                false
            )
        )
    }

    //cargar datos guardados

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

        //título

        Text(
            text = "Inicio de sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        //email

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

        //contraseña

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

        //recordar sesión

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

        //botón login

        Button(
            onClick = {

                mensajeError = ""

                //validar campos vacíos

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

                        //guardar datos

                        if (recordarSesion) {

                            prefs.edit()
                                .putString(
                                    "email",
                                    usuario.trim()
                                )
                                .putString(
                                    "password",
                                    password.trim()
                                )
                                .putBoolean(
                                    "recordar",
                                    true
                                )
                                .apply()

                        } else {

                            prefs.edit()
                                .clear()
                                .apply()
                        }

                        viewModel.sincronizarFirebase()
                        onLoginCorrecto()

                    } else {

                        mensajeError =
                            "Usuario o contraseña incorrectos"
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

        //mostrar mensaje de error

        if (mensajeError.isNotEmpty()) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        //recuperar contraseña

        TextButton(
            onClick = {
                onForgotPasswordClick()
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("¿Olvidaste tu contraseña?")
        }

        Spacer(modifier = Modifier.height(4.dp))

        //registro

        TextButton(
            onClick = {
                onRegisterClick()
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("¿No tienes cuenta? Regístrate")
        }

        Spacer(modifier = Modifier.height(4.dp))

        //invitado

        TextButton(
            onClick = {
                onGuestLogin()
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Entrar como invitado")
        }
    }
}