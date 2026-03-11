package com.example.rolcraft.Personaje

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState

@Composable
fun PantallaCrearPersonaje2(
    viewModel: PersonajeViewModel,
    onAnterior: () -> Unit,
    onSiguiente: () -> Unit
) {
    val pj = viewModel.personaje

    var campoFaltante by remember { mutableStateOf<String?>(null) }

    if (campoFaltante != null) {

        val alpha by animateFloatAsState(1f, tween(250))
        val offsetY by animateDpAsState(0.dp, tween(250))

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
            text = { Text("Debes rellenar: $campoFaltante") },
            confirmButton = {
                TextButton(onClick = { campoFaltante = null }) {
                    Text("Entendido")
                }
            },
            containerColor = Color(0xFFF5E6C8),
            titleContentColor = Color(0xFF3E2723),
            textContentColor = Color(0xFF3E2723)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable { campoFaltante = null }
    ) {

        Text(
            text = "Crea tu personaje",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        Desplegable(
            titulo = "Clase",
            opciones = listaClases,
            valor = pj.clase,
            descripcion = descripcionClase[pj.clase],
            onSelect = { viewModel.actualizarClase(it) }
        )

        Spacer(Modifier.height(16.dp))

        Desplegable(
            titulo = "Subclase",
            opciones = listaSubclases[pj.clase] ?: emptyList(),
            valor = pj.subclase,
            descripcion = descripcionSubclase[pj.subclase],
            onSelect = { viewModel.actualizarSubclase(it) }
        )

        Spacer(Modifier.height(16.dp))

        Desplegable(
            titulo = "Trasfondo",
            opciones = listaTrasfondos,
            valor = pj.trasfondo,
            descripcion = descripcionTrasfondo[pj.trasfondo],
            onSelect = { viewModel.actualizarTrasfondo(it) }
        )

        Spacer(Modifier.height(16.dp))

        Desplegable(
            titulo = "Alineamiento",
            opciones = listaAlineamientos,
            valor = pj.alineamiento,
            descripcion = descripcionAlineamiento[pj.alineamiento],
            onSelect = { viewModel.actualizarAlineamiento(it) }
        )

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onAnterior) {
                Text("Anterior")
            }

            Button(
                onClick = {
                    val falta = viewModel.validarCampos()
                    if (falta != null) campoFaltante = falta
                    else onSiguiente()
                }
            ) {
                Text("Siguiente")
            }
        }
    }
}