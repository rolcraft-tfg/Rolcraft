package com.example.rolcraft.CrearPersonaje

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.rolcraft.R

// IMAGEN
fun obtenerImagenClase(clase: String): Int {
    return when (clase.lowercase().trim()) {
        "mago" -> R.drawable.mago
        "clérigo", "clerigo" -> R.drawable.clerigo
        "guerrero" -> R.drawable.guerrero
        "pícaro", "picaro" -> R.drawable.picaro
        else -> R.drawable.personaje_placeholder
    }
}

// COLOR
fun obtenerColorClase(clase: String): Color {
    return when (clase.lowercase().trim()) {
        "mago" -> Color(0xFF2196F3)
        "guerrero" -> Color(0xFFE53935)
        "pícaro", "picaro" -> Color(0xFF43A047)
        "clérigo", "clerigo" -> Color(0xFFFDD835)
        else -> Color.Gray
    }
}

@Composable
fun PantallaCrearPersonaje(
    viewModel: PersonajeViewModel,
    onSiguiente: () -> Unit,
    onVolver: () -> Unit
) {

    val pj = viewModel.personaje
    val modoEdicion = viewModel.modoEdicion

    var campoFaltante by remember { mutableStateOf<String?>(null) }

    // DIALOGO
    if (campoFaltante != null) {

        val alpha by animateFloatAsState(1f, tween(250), label = "")
        val offsetY by animateDpAsState(0.dp, tween(250), label = "")

        AlertDialog(
            onDismissRequest = { campoFaltante = null },
            modifier = Modifier.graphicsLayer {
                this.alpha = alpha
                translationY = offsetY.toPx()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, null, tint = Color.Red)
                    Spacer(Modifier.width(8.dp))
                    Text("Falta un campo")
                }
            },
            text = { Text("Debes rellenar: $campoFaltante") },
            confirmButton = {
                TextButton(onClick = { campoFaltante = null }) {
                    Text("Entendido")
                }
            }
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

        // HEADER NUEVO
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = if (modoEdicion) "Editar personaje" else "Crea tu personaje",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { viewModel.generarAleatorio() }) {
                    Text("Aleatorio")
                }
            }

            // IMAGEN DINÁMICA
            Card(
                modifier = Modifier.size(100.dp),
                border = BorderStroke(2.dp, obtenerColorClase(pj.clase)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Image(
                    painter = painterResource(
                        id = obtenerImagenClase(pj.clase)
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TEXTFIELD
        val nombreState = remember {
            mutableStateOf(TextFieldValue(pj.nombre))
        }

        LaunchedEffect(pj.nombre) {
            if (nombreState.value.text != pj.nombre) {
                nombreState.value = TextFieldValue(pj.nombre)
            }
        }

        TextField(
            value = nombreState.value,
            onValueChange = { nuevo ->
                nombreState.value = nuevo
                viewModel.actualizarNombre(nuevo.text)
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text("${nombreState.value.text.length}/20")
            },
            isError = nombreState.value.text.length >= 20,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable("Género", listaGeneros, pj.genero, null) {
            viewModel.actualizarGenero(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable("Raza", listaRazas, pj.raza, descripcionRaza[pj.raza]) {
            viewModel.actualizarRaza(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable("Clase", listaClases, pj.clase, descripcionClase[pj.clase]) {
            viewModel.actualizarClase(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable(
            "Subclase",
            listaSubclases[pj.clase] ?: emptyList(),
            pj.subclase,
            descripcionSubclase[pj.subclase]
        ) {
            viewModel.actualizarSubclase(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable("Trasfondo", listaTrasfondos, pj.trasfondo, descripcionTrasfondo[pj.trasfondo]) {
            viewModel.actualizarTrasfondo(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Desplegable("Alineamiento", listaAlineamientos, pj.alineamiento, descripcionAlineamiento[pj.alineamiento]) {
            viewModel.actualizarAlineamiento(it)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val falta = viewModel.validarCampos()
                if (falta != null) campoFaltante = falta
                else onSiguiente()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(if (modoEdicion) "Guardar cambios" else "Ver ficha")
        }
    }
}