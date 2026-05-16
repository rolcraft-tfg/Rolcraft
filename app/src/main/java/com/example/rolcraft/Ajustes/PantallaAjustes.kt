package com.example.rolcraft.Ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.rolcraft.Dados.DiceTheme
import com.example.rolcraft.Registro.esPasswordValida
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun PantallaAjustes(
    modoOscuro: Boolean,
    onCambiarTema: (Boolean) -> Unit,
    onCerrarSesion: () -> Unit
) {

    // FIREBASE

    val usuarioActual =
        FirebaseAuth.getInstance().currentUser

    val esInvitado =
        usuarioActual == null

    // DIALOG CAMBIAR NOMBRE

    var mostrarDialogNombre by remember {
        mutableStateOf(false)
    }

    var nuevoNombre by remember {
        mutableStateOf("")
    }

    // DIALOG CAMBIAR PASSWORD

    var mostrarDialogPassword by remember {
        mutableStateOf(false)
    }

    var passwordActual by remember {
        mutableStateOf("")
    }

    var nuevaPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    // DIALOG ELIMINAR

    var mostrarDialogEliminar by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp)
    ) {

        // TÍTULO

        Text(
            text = "Ajustes",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // USUARIO ACTUAL

        Text(
            text = "Conectado como:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text =
                if (esInvitado)
                    "Invitado"
                else
                    usuarioActual?.displayName ?: "Usuario",

            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // OPCIONES SOLO USUARIO

        if (!esInvitado) {

            // CAMBIAR NOMBRE

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

                onClick = {

                    nuevoNombre =
                        usuarioActual?.displayName ?: ""

                    mostrarDialogNombre = true
                }
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Usuario",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Cambiar nombre de usuario",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CAMBIAR CONTRASEÑA

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

                onClick = {

                    passwordActual = ""
                    nuevaPassword = ""

                    mostrarDialogPassword = true
                }
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Contraseña",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Actualizar contraseña de acceso",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ELIMINAR CUENTA

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

                onClick = {

                    mostrarDialogEliminar = true
                }
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Eliminar cuenta",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Borrar permanentemente tu cuenta",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // MODO OSCURO

        Card(
            modifier = Modifier.fillMaxWidth(),

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Modo oscuro",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text =
                            if (modoOscuro)
                                "Activado"
                            else
                                "Desactivado",

                        style = MaterialTheme.typography.bodySmall,

                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = modoOscuro,

                    onCheckedChange = {
                        onCambiarTema(it)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // INFO APP

        Column(
            modifier = Modifier.fillMaxWidth(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "RolCraft v1.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // DIALOG CAMBIAR NOMBRE

    if (mostrarDialogNombre) {

        AlertDialog(

            onDismissRequest = {
                mostrarDialogNombre = false
            },

            title = {
                Text("Cambiar nombre")
            },

            text = {

                OutlinedTextField(
                    value = nuevoNombre,

                    onValueChange = {
                        nuevoNombre = it
                    },

                    label = {
                        Text("Nuevo nombre")
                    },

                    singleLine = true
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        val profileUpdates =
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(
                                    nuevoNombre.trim()
                                )
                                .build()

                        usuarioActual
                            ?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener {

                                mostrarDialogNombre = false
                            }
                    }
                ) {

                    Text("Guardar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarDialogNombre = false
                    }
                ) {

                    Text("Cancelar")
                }
            }
        )
    }

    // DIALOG CAMBIAR PASSWORD

    if (mostrarDialogPassword) {

        var mensajePassword by remember {
            mutableStateOf("")
        }

        AlertDialog(

            onDismissRequest = {
                mostrarDialogPassword = false
            },

            title = {
                Text("Cambiar contraseña")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = passwordActual,

                        onValueChange = {

                            passwordActual = it

                            mensajePassword = ""
                        },

                        label = {
                            Text("Contraseña actual")
                        },

                        singleLine = true,

                        visualTransformation =
                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nuevaPassword,

                        onValueChange = {

                            nuevaPassword = it

                            mensajePassword = ""
                        },

                        label = {
                            Text("Nueva contraseña")
                        },

                        singleLine = true,

                        visualTransformation =
                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation()
                    )

                    if (mensajePassword.isNotEmpty()) {

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = mensajePassword,

                            color =
                                if (
                                    mensajePassword.contains("actualizada")
                                )
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error
                        )
                    }
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        if (!esPasswordValida(nuevaPassword)) {

                            mensajePassword =
                                "La contraseña debe tener mínimo 8 caracteres"

                            return@TextButton
                        }

                        val email =
                            usuarioActual?.email

                        if (
                            email == null ||
                            passwordActual.isBlank()
                        ) {

                            mensajePassword =
                                "Introduce tu contraseña actual"

                            return@TextButton
                        }

                        val credential =
                            EmailAuthProvider.getCredential(
                                email,
                                passwordActual.trim()
                            )

                        usuarioActual
                            .reauthenticate(credential)
                            .addOnCompleteListener { authTask ->

                                if (authTask.isSuccessful) {

                                    usuarioActual
                                        .updatePassword(
                                            nuevaPassword.trim()
                                        )
                                        .addOnCompleteListener { task ->

                                            if (task.isSuccessful) {

                                                mensajePassword =
                                                    "Contraseña actualizada"

                                            } else {

                                                mensajePassword =
                                                    "Error al actualizar contraseña"
                                            }
                                        }

                                } else {

                                    mensajePassword =
                                        "La contraseña actual es incorrecta"
                                }
                            }
                    }
                ) {

                    Text("Actualizar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarDialogPassword = false
                    }
                ) {

                    Text("Atrás")
                }
            }
        )
    }

    // DIALOG ELIMINAR CUENTA

    if (mostrarDialogEliminar) {

        AlertDialog(

            onDismissRequest = {
                mostrarDialogEliminar = false
            },

            title = {
                Text("Eliminar cuenta")
            },

            text = {
                Text(
                    "¿Seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        usuarioActual
                            ?.delete()
                            ?.addOnCompleteListener { task ->

                                if (task.isSuccessful) {

                                    FirebaseAuth
                                        .getInstance()
                                        .signOut()

                                    mostrarDialogEliminar = false

                                    onCerrarSesion()
                                }
                            }
                    }
                ) {

                    Text("Eliminar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarDialogEliminar = false
                    }
                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}