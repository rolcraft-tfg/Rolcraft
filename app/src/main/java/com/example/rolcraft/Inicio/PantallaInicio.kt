package com.example.rolcraft.Inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rolcraft.CrearPersonaje.Personaje
import com.example.rolcraft.CrearPersonaje.PersonajeViewModel
import com.example.rolcraft.R

@Composable
fun PantallaInicio(
    viewModel: PersonajeViewModel,
    onCrearPersonaje: () -> Unit,
    onPantallaFichaPersonaje: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.cargarPersonajes()
    }

    val personajes = viewModel.personajesGuardados

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(
                    text = "Mis personajes",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(personajes) { personaje ->
                        TarjetaPersonaje(
                            personaje = personaje,

                            onEliminar = {
                                viewModel.eliminarPersonaje(personaje)
                            },

                            onDuplicar = { pj ->
                                val copia = pj.copy(nombre = "${pj.nombre} Copy")
                                viewModel.insertarPersonaje(copia)
                            },

                            // 🔥 EDITAR CONECTADO
                            onEditar = { pj ->
                                viewModel.empezarEdicion(pj)
                                onCrearPersonaje()
                            },

                            onClick = onPantallaFichaPersonaje
                        )
                    }
                }
            }

            // 💚 BOTÓN ABAJO
            Button(
                onClick = { onCrearPersonaje() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2ECC71)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crear nuevo personaje", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TarjetaPersonaje(
    personaje: Personaje,
    onEliminar: () -> Unit,
    onDuplicar: (Personaje) -> Unit,
    onEditar: (Personaje) -> Unit,
    onClick: () -> Unit
) {

    var mostrarDialogo by remember { mutableStateOf(false) }
    var menuExpandido by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Eliminar personaje") },
            text = { Text("¿Seguro que quieres borrar el personaje?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    onEliminar()
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                }) {
                    Text("No")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.personaje_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = personaje.nombre,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Nivel 1 • ${personaje.raza}",
                    color = Color.LightGray
                )

                Text(
                    text = personaje.clase,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 🔥 MENÚ 3 PUNTOS
            Box {
                IconButton(
                    onClick = { menuExpandido = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }

                DropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false }
                ) {

                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            menuExpandido = false
                            onEditar(personaje)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Duplicar") },
                        onClick = {
                            menuExpandido = false
                            onDuplicar(personaje)
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Eliminar", color = Color.Red)
                        },
                        onClick = {
                            menuExpandido = false
                            mostrarDialogo = true
                        }
                    )
                }
            }
        }
    }
}