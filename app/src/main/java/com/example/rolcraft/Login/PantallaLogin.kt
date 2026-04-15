package com.example.rolcraft.Login

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

@Composable
fun PantallaLogin(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 👁️ CONTROL DEL OJITO
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // ⭐ TÍTULO
        Text(
            text = "Inicio de sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ⭐ USUARIO / EMAIL
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Nombre de usuario o email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ⭐ CONTRASEÑA
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,

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

        Spacer(modifier = Modifier.height(32.dp))

        // ⭐ BOTÓN LOGIN
        Button(
            onClick = {
                onLoginClick(usuario, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ⭐ RECUPERAR CONTRASEÑA
        TextButton(
            onClick = { onForgotPasswordClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Olvidaste tu contraseña?")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ⭐ REGISTRO
        TextButton(
            onClick = { onRegisterClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}