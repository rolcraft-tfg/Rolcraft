package com.example.rolcraft.Inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MenuBook
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
    onDados: () -> Unit,
    onCampania: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    // Cargar desde Room al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarPersonajes()
    }

    // Lista desde ViewModel
    val personajes = viewModel.personajesGuardados

    Scaffold(
        containerColor = Color(0xFF0F1720),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF16202A)
            ) {

                NavigationBarItem(
                    selected = false,
                    onClick = onCrearPersonaje,
                    icon = { Icon(Icons.Default.Add, null) },
                    label = { Text("Crear") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onDados,
                    icon = { Icon(Icons.Default.Casino, null) },
                    label = { Text("Dados") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onCampania,
                    icon = { Icon(Icons.Default.MenuBook, null) },
                    label = { Text("Mi campaña") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onCerrarSesion,
                    icon = { Icon(Icons.Default.ExitToApp, null) },
                    label = { Text("Salir") }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F1720))
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Mis personajes",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(personajes) { personaje ->
                    TarjetaPersonaje(personaje)
                }
            }
        }
    }
}

@Composable
fun TarjetaPersonaje(personaje: Personaje) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B2733)
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

            Column {

                Text(
                    text = personaje.nombre,
                    color = Color.White,
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
                    color = Color(0xFF7EA7FF)
                )
            }
        }
    }
}