package com.example.rolcraft.Personaje

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rolcraft.Personaje.PersonajeViewModel

@Composable
fun PantallaCrearPersonaje1(
    viewModel: PersonajeViewModel,
    onSiguiente: () -> Unit
) {
    val pj = viewModel.personaje

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Crea tu personaje",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        Button(onClick = { viewModel.generarAleatorio() }) {
            Text("Aleatorio")
        }

        Spacer(Modifier.height(16.dp))

        TextField(
            value = pj.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Desplegable(
            titulo = "Género",
            opciones = listaGeneros,
            valor = pj.genero,
            descripcion = null,
            onSelect = { viewModel.actualizarGenero(it) }
        )

        Spacer(Modifier.height(16.dp))

        Desplegable(
            titulo = "Raza",
            opciones = listaRazas,
            valor = pj.raza,
            descripcion = descripcionRaza[pj.raza],
            onSelect = { viewModel.actualizarRaza(it) }
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onSiguiente,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Siguiente")
        }
    }
}
