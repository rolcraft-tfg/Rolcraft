package com.example.rolcraft.Registro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

// VALIDACIÓN PASSWORD
fun esPasswordValida(password: String): Boolean {

    val regex = Regex("^.{8,}$")

    return regex.matches(password)
}

@Composable
fun PantallaRegistro(
    onVolver: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        // USUARIO
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // PASSWORD
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = !esPasswordValida(it)
                confirmPasswordError = confirmPassword.isNotEmpty() && confirmPassword != it
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = passwordError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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

        // CONFIRM PASSWORD
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = it != password
            },
            label = { Text("Repetir contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = confirmPasswordError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        )

        if (confirmPasswordError && confirmPassword.isNotBlank()) {
            Text(
                text = "Las contraseñas no coinciden",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(24.dp))

        // BOTÓN REGISTRO
        Button(
            onClick = {
                mensaje = ""

                if (usuario.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    mensaje = "Faltan campos por rellenar"
                    return@Button
                }

                if (!esPasswordValida(password)) {
                    mensaje = "La contraseña debe tener mínimo 8 caracteres"
                    return@Button
                }

                if (password != confirmPassword) {
                    mensaje = "Las contraseñas no coinciden"
                    return@Button
                }

                cargando = true

                auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                    .addOnCompleteListener { task ->

                        cargando = false

                        if (task.isSuccessful) {

                            val user = auth.currentUser

                            // Actualizar nombre
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(usuario.trim())
                                .build()

                            user?.updateProfile(profileUpdates)

                            // Enviar email de verificación
                            user?.sendEmailVerification()

                            mensaje = "Revisa tu correo para verificar la cuenta"

                            // Cerrar sesión
                            auth.signOut()

                        } else {
                            mensaje = "No se pudo registrar el usuario"
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

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = mensaje,
                color = if (mensaje.contains("correo")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(12.dp))

        TextButton(
            onClick = { onVolver() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al login")
        }
    }
}