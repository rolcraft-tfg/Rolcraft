package com.example.rolcraft.Registro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

//iconos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

//firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

//función para validar que la contraseña tenga un mínimo de 8 caracteres
fun esPasswordValida(password: String): Boolean {

    val regex = Regex("^.{8,}$")

    return regex.matches(password)
}

@Composable
fun PantallaRegistro(
    onVolver: () -> Unit
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    //control de la contraseña

    var passwordError by remember {
        mutableStateOf(false)
    }

    var confirmPasswordError by remember {
        mutableStateOf(false)
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    //mensajes

    var mensaje by remember {
        mutableStateOf("")
    }

    //controla el estado de carga mientras se realiza el login o registro
    var cargando by remember {
        mutableStateOf(false)
    }

    //Autentificación de firebase

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {

        //título

        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(32.dp))

        //usuario

        OutlinedTextField(
            value = usuario,

            onValueChange = {
                usuario = it
            },

            label = {
                Text("Nombre de usuario")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        //email

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
            },

            label = {
                Text("Email")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        //Texto para introducir la contraseña

        OutlinedTextField(
            value = password,

            onValueChange = {

                password = it

                passwordError =
                    !esPasswordValida(it)

                confirmPasswordError =
                    confirmPassword.isNotEmpty() &&
                            confirmPassword != it
            },

            label = {
                Text("Contraseña")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            isError = passwordError,

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

        if (passwordError && password.isNotBlank()) {

            Text(
                text = "La contraseña debe tener mínimo 8 caracteres",

                color = MaterialTheme.colorScheme.error,

                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(12.dp))

        //confirmar contraseña

        OutlinedTextField(
            value = confirmPassword,

            onValueChange = {

                confirmPassword = it

                confirmPasswordError =
                    it != password
            },

            label = {
                Text("Repetir contraseña")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            isError = confirmPasswordError,

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

        if (
            confirmPasswordError &&
            confirmPassword.isNotBlank()
        ) {

            Text(
                text = "Las contraseñas no coinciden",

                color = MaterialTheme.colorScheme.error,

                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(24.dp))

        //botón de registro

        Button(
            onClick = {

                mensaje = ""

                //validar campos vacíos

                if (
                    usuario.isBlank() ||
                    email.isBlank() ||
                    password.isBlank() ||
                    confirmPassword.isBlank()
                ) {

                    mensaje =
                        "Faltan campos por rellenar"

                    return@Button
                }

                //validar password

                if (!esPasswordValida(password)) {

                    mensaje =
                        "La contraseña debe tener mínimo 8 caracteres"

                    return@Button
                }

                //validar coincidencia de ambas contraseñas

                if (password != confirmPassword) {

                    mensaje =
                        "Las contraseñas no coinciden"

                    return@Button
                }

                cargando = true

                auth.createUserWithEmailAndPassword(
                    email.trim(),
                    password.trim()
                ).addOnCompleteListener { task ->

                    cargando = false

                    if (task.isSuccessful) {

                        val profileUpdates =
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(usuario.trim())
                                .build()

                        auth.currentUser
                            ?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener {

                                mensaje =
                                    "Usuario registrado correctamente"
                            }

                    } else {

                        mensaje =
                            "No se pudo registrar el usuario"
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

                Text("Registrarse")
            }
        }

        //mensaje

        if (mensaje.isNotEmpty()) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensaje,

                color = if (
                    mensaje.contains("correctamente")
                )
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(12.dp))

        //volver al login

        TextButton(
            onClick = {
                onVolver()
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Volver al login")
        }
    }
}