package com.example.rolcraft.RecuperarContrasenya

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaRecuperar(
    onVolver: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    // Mensaje

    var mensaje by remember {
        mutableStateOf("")
    }

    // Carga de la página

    var cargando by remember {
        mutableStateOf(false)
    }

    // Implementación del firebase

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        // Descripción

        Text(
            text = "Introduce tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña (recuerda mirar en spam).",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        // Email

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

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {

                mensaje = ""

                if (email.isBlank()) {

                    mensaje =
                        "Introduce un email"

                    return@Button
                }

                // Validar formato email

                if (
                    !Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches()
                ) {

                    mensaje =
                        "Introduce un email válido"

                    return@Button
                }

                cargando = true

                // Enviar correo

                auth.sendPasswordResetEmail(
                    email.trim()
                ).addOnCompleteListener { task ->

                    cargando = false

                    if (task.isSuccessful) {

                        mensaje =
                            "Correo de recuperación enviado"

                    } else {

                        mensaje =
                            task.exception?.message
                                ?: "No se pudo enviar el correo"
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

                Text("Enviar correo")
            }
        }

        // Mensaje de texto que aparece debajo del botón dependiendo de lo que ocurra

        if (mensaje.isNotEmpty()) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensaje,

                color = if (
                    mensaje.contains("enviado")
                )
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(12.dp))

        // Volver al login

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