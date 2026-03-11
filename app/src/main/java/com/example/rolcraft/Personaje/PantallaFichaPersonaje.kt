package com.example.rolcraft.Personaje

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState

@Composable
fun PantallaFichaPersonaje(
    viewModel: PersonajeViewModel,
    onAnterior: () -> Unit,
    onGuardar: () -> Unit,
    onNuevoPersonaje: () -> Unit
) {
    val pj = viewModel.personaje

    var guardadoCorrecto by remember { mutableStateOf(false) }

    if (guardadoCorrecto) {

        val alpha by animateFloatAsState(1f, tween(250))
        val offsetY by animateDpAsState(0.dp, tween(250))

        AlertDialog(
            onDismissRequest = { guardadoCorrecto = false },
            modifier = Modifier.graphicsLayer {
                this.alpha = alpha
                translationY = offsetY.toPx()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("¡Personaje guardado!")
                }
            },
            text = { Text("Personaje guardado correctamente.") },
            confirmButton = {
                TextButton(onClick = { guardadoCorrecto = false }) {
                    Text("Perfecto")
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
            .background(Color(0xFFF5E6C8))
            .padding(24.dp)
    ) {

        Text(
            text = "Ficha del Personaje",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF3E2723)
        )

        Spacer(Modifier.height(24.dp))

        FichaCampo("Nombre", pj.nombre)
        FichaCampo("Género", pj.genero)
        FichaCampo("Raza", pj.raza)
        FichaCampo("Clase", pj.clase)
        FichaCampo("Subclase", pj.subclase)
        FichaCampo("Trasfondo", pj.trasfondo)
        FichaCampo("Alineamiento", pj.alineamiento)

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onAnterior) {
                Text("Anterior")
            }

            Button(onClick = {
                guardadoCorrecto = true
                onGuardar()
            }) {
                Text("Guardar personaje")
            }

            Button(onClick = {
                viewModel.resetearPersonaje()
                onNuevoPersonaje()
            }) {
                Text("Crear otro")
            }
        }
    }
}

@Composable
fun FichaCampo(titulo: String, valor: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF3E2723)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5D4037)
        )
    }
}