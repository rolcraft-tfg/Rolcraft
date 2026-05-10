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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaLogin(
    onLoginCorrecto: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGuestLogin: () -> Unit
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    val auth = FirebaseAuth.getInstance()

    // =========================
    // SHARED PREFERENCES
    // =========================

    val context = LocalContext.current

    val prefs = context.getSharedPreferences(
        "login_prefs",
        Context.MODE_PRIVATE
    )

    // =========================
    // RECORDAR SESIÓN
    // =========================

    var recordarSesion by remember {

        mutableStateOf(
            prefs.getBoolean(
                "recordar",
                false
            )
        )
    }

    // =========================
    // CARGAR DATOS GUARDADOS
    // =========================

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

        // =========================
        // TÍTULO
        // =========================

        Text(
            text = "Inicio de sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        // =========================
        // EMAIL
        // =========================

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

        // =========================
        // CONTRASEÑA
        // =========================

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

        // =========================
        // RECORDAR SESIÓN
        // =========================

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

        // =========================
        // BOTÓN LOGIN
        // =========================

        Button(
            onClick = {

                mensajeError = ""

                // VALIDAR CAMPOS VACÍOS

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

                        // =========================
                        // GUARDAR DATOS
                        // =========================

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

        // =========================
        // MENSAJE ERROR
        // =========================

        if (mensajeError.isNotEmpty()) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // =========================
        // RECUPERAR
        // =========================

        TextButton(
            onClick = {
                onForgotPasswordClick()
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("¿Olvidaste tu contraseña?")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // =========================
        // REGISTRO
        // =========================

        TextButton(
            onClick = {
                onRegisterClick()
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("¿No tienes cuenta? Regístrate")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // =========================
        // INVITADO
        // =========================

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