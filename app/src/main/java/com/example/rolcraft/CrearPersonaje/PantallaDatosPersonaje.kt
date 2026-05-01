package com.example.rolcraft.CrearPersonaje

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaDatosPersonaje(
    viewModel: PersonajeViewModel,
    onAnterior: () -> Unit,
    onGuardar: () -> Unit,
    onNuevoPersonaje: () -> Unit
) {

    val personaje = viewModel.personaje

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // fondo oscuro
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        TextButton(
            onClick = onAnterior
        ) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ficha del personaje",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface // tarjeta oscura
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = personaje.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                FilaDato("Género", personaje.genero)
                FilaDato("Raza", personaje.raza)
                FilaDato("Clase", personaje.clase)
                FilaDato("Subclase", personaje.subclase)
                FilaDato("Trasfondo", personaje.trasfondo)
                FilaDato("Alineamiento", personaje.alineamiento)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = onGuardar,
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }

            Button(
                onClick = onNuevoPersonaje,
                modifier = Modifier.weight(1f)
            ) {
                Text("Nuevo")
            }
        }
    }
}

@Composable
private fun FilaDato(
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "$titulo:",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = valor,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}