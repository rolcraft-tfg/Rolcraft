package com.example.rolcraft.CrearPersonaje

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCrearPersonaje(
    viewModel: PersonajeViewModel,

    // ⭐ Ir a la ficha del personaje
    onSiguiente: () -> Unit,

    // ⭐ Volver a la pantalla principal
    onVolver: () -> Unit
) {

    val pj = viewModel.personaje

    // ⭐ Variable para mostrar qué campo falta si el usuario no rellena algo
    var campoFaltante by remember { mutableStateOf<String?>(null) }

    // ⭐ Diálogo de error si falta algún campo obligatorio
    if (campoFaltante != null) {

        val alpha by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(250),
            label = "alpha"
        )

        val offsetY by animateDpAsState(
            targetValue = 0.dp,
            animationSpec = tween(250),
            label = "offset"
        )

        AlertDialog(
            onDismissRequest = { campoFaltante = null },
            modifier = Modifier.graphicsLayer {
                this.alpha = alpha
                translationY = offsetY.toPx()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFF8B0000),
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text("Falta un campo")
                }
            },
            text = {
                Text("Debes rellenar: $campoFaltante")
            },
            confirmButton = {
                TextButton(
                    onClick = { campoFaltante = null }
                ) {
                    Text("Entendido")
                }
            },
            containerColor = Color(0xFFF5E6C8),
            titleContentColor = Color(0xFF3E2723),
            textContentColor = Color(0xFF3E2723)
        )
    }

    // ⭐ Toda la pantalla ahora tiene scroll vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .clickable {
                campoFaltante = null
            }
    ) {

        // ⭐ Botón para volver a la pantalla principal
        TextButton(
            onClick = {
                onVolver()
            }
        ) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ⭐ Título principal
        Text(
            text = "Crea tu personaje",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ⭐ Botón para generar un personaje aleatorio
        Button(
            onClick = {
                viewModel.generarAleatorio()
            }
        ) {
            Text("Aleatorio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ⭐ Campo nombre
        TextField(
            value = pj.nombre,
            onValueChange = {
                viewModel.actualizarNombre(it)
            },
            label = {
                Text("Nombre")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ⭐ Selector de género
        Desplegable(
            titulo = "Género",
            opciones = listaGeneros,
            valor = pj.genero,
            descripcion = null,
            onSelect = {
                viewModel.actualizarGenero(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ⭐ Selector de raza
        Desplegable(
            titulo = "Raza",
            opciones = listaRazas,
            valor = pj.raza,
            descripcion = descripcionRaza[pj.raza],
            onSelect = {
                viewModel.actualizarRaza(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ⭐ Selector de clase
        Desplegable(
            titulo = "Clase",
            opciones = listaClases,
            valor = pj.clase,
            descripcion = descripcionClase[pj.clase],
            onSelect = {
                viewModel.actualizarClase(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ⭐ Selector de subclase
        Desplegable(
            titulo = "Subclase",
            opciones = listaSubclases[pj.clase] ?: emptyList(),
            valor = pj.subclase,
            descripcion = descripcionSubclase[pj.subclase],
            onSelect = {
                viewModel.actualizarSubclase(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ⭐ Selector de trasfondo
        Desplegable(
            titulo = "Trasfondo",
            opciones = listaTrasfondos,
            valor = pj.trasfondo,
            descripcion = descripcionTrasfondo[pj.trasfondo],
            onSelect = {
                viewModel.actualizarTrasfondo(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ⭐ Selector de alineamiento
        Desplegable(
            titulo = "Alineamiento",
            opciones = listaAlineamientos,
            valor = pj.alineamiento,
            descripcion = descripcionAlineamiento[pj.alineamiento],
            onSelect = {
                viewModel.actualizarAlineamiento(it)
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ⭐ Botón final para ir a la ficha del personaje
        Button(
            onClick = {

                val falta = viewModel.validarCampos()

                if (falta != null) {
                    campoFaltante = falta
                } else {
                    onSiguiente()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text("Ver ficha")
        }
    }
}