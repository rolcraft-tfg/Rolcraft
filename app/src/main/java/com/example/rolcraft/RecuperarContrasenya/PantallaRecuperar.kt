package com.example.rolcraft.RecuperarContrasenya

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// ⭐ ICONOS
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.example.rolcraft.Registro.esPasswordValida

@Composable
fun PantallaRecuperar(
    onVolver: () -> Unit
) {

    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // ⭐ TÍTULO
        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(32.dp))

        // ⭐ USUARIO / EMAIL
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario o email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // ⭐ NUEVA CONTRASEÑA
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = !esPasswordValida(it)
                confirmPasswordError = confirmPassword.isNotEmpty() && confirmPassword != it
            },
            label = { Text("Nueva contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = passwordError,

            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),

            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        )

        if (passwordError) {
            Text(
                text = "Debe tener 8 caracteres, una mayúscula, un número y un símbolo",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(12.dp))

        // ⭐ CONFIRMAR CONTRASEÑA
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = it != password
            },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = confirmPasswordError,

            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),

            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        )

        if (confirmPasswordError) {
            Text(
                text = "Las contraseñas no coinciden",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(24.dp))

        // ⭐ BOTÓN CAMBIAR CONTRASEÑA
        Button(
            onClick = { /* luego Room */ },
            enabled = esPasswordValida(password) && password == confirmPassword,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Cambiar contraseña")
        }

        Spacer(Modifier.height(12.dp))

        // ⭐ VOLVER
        TextButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al login")
        }
    }
}