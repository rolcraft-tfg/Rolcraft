package com.example.rolcraft.CrearPersonaje

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCrearPersonaje(
    viewModel: PersonajeViewModel,
    onSiguiente: () -> Unit,
    onVolver: () -> Unit
) {

    val pj = viewModel.personaje
    val modoEdicion = viewModel.modoEdicion

    var campoFaltante by remember { mutableStateOf<String?>(null) }

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
                        tint = Color.Red,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text("Falta un campo")
                }
            },
            text = {
                Text("Debes rellenar: $campoFaltante")
            },
            confirmButton = {
                TextButton(onClick = { campoFaltante = null }) {
                    Text("Entendido")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .clickable { campoFaltante = null }
            .pointerInput(Unit) {}
    ) {

        TextButton(onClick = { onVolver() }) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (modoEdicion) "Editar personaje" else "Crea tu personaje",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.generarAleatorio() }
        ) {
            Text("Aleatorio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = pj.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable(
            titulo = "Género",
            opciones = listaGeneros,
            valor = pj.genero,
            descripcion = null,
            onSelect = { viewModel.actualizarGenero(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable(
            titulo = "Raza",
            opciones = listaRazas,
            valor = pj.raza,
            descripcion = descripcionRaza[pj.raza],
            onSelect = { viewModel.actualizarRaza(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable(
            titulo = "Clase",
            opciones = listaClases,
            valor = pj.clase,
            descripcion = descripcionClase[pj.clase],
            onSelect = { viewModel.actualizarClase(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable(
            titulo = "Subclase",
            opciones = listaSubclases[pj.clase] ?: emptyList(),
            valor = pj.subclase,
            descripcion = descripcionSubclase[pj.subclase],
            onSelect = { viewModel.actualizarSubclase(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable(
            titulo = "Trasfondo",
            opciones = listaTrasfondos,
            valor = pj.trasfondo,
            descripcion = descripcionTrasfondo[pj.trasfondo],
            onSelect = { viewModel.actualizarTrasfondo(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable(
            titulo = "Alineamiento",
            opciones = listaAlineamientos,
            valor = pj.alineamiento,
            descripcion = descripcionAlineamiento[pj.alineamiento],
            onSelect = { viewModel.actualizarAlineamiento(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // BOTÓN INTELIGENTE
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
            Text(
                if (modoEdicion) "Guardar cambios" else "Ver ficha"
            )
        }
    }
}