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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rolcraft.CrearPersonaje.Personaje
import com.example.rolcraft.R

@Composable
fun PantallaInicio(
    personajes: List<Personaje>,
    onCrearPersonaje: () -> Unit,
    onDados: () -> Unit,
    onCampania: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    // ⭐ Scaffold para tener la barra inferior fija
    Scaffold(

        // ⭐ Color de fondo general de la pantalla
        containerColor = Color(0xFF0F1720),

        // ⭐ Barra inferior estilo D&D Beyond
        bottomBar = {

            NavigationBar(
                containerColor = Color(0xFF16202A)
            ) {

                // ⭐ Botón crear personaje
                NavigationBarItem(
                    selected = false,
                    onClick = onCrearPersonaje,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear personaje"
                        )
                    },
                    label = {
                        Text("Crear")
                    }
                )

                // ⭐ Botón dados
                NavigationBarItem(
                    selected = false,
                    onClick = onDados,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = "Dados"
                        )
                    },
                    label = {
                        Text("Dados")
                    }
                )

                // ⭐ Botón mi campaña
                NavigationBarItem(
                    selected = false,
                    onClick = onCampania,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Mi campaña"
                        )
                    },
                    label = {
                        Text("Mi campaña")
                    }
                )

                // ⭐ Botón cerrar sesión
                NavigationBarItem(
                    selected = false,
                    onClick = onCerrarSesion,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    },
                    label = {
                        Text("Salir")
                    }
                )
            }
        }
    ) { padding ->

        // ⭐ Contenido principal de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F1720))
                .padding(padding)
                .padding(16.dp)
        ) {

            // ⭐ Título de la pantalla
            Text(
                text = "Mis personajes",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ⭐ Lista de personajes guardados
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ⭐ Recorre la lista de personajes y crea una tarjeta por cada uno
                items(personajes) { personaje ->

                    TarjetaPersonaje(personaje)
                }
            }
        }
    }
}

@Composable
fun TarjetaPersonaje(personaje: Personaje) {

    // ⭐ Tarjeta individual de personaje
    Card(
        modifier = Modifier.fillMaxWidth(),

        // ⭐ Color oscuro para la tarjeta
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

            // ⭐ Imagen del personaje a la izquierda
            // Usa una imagen temporal llamada personaje_placeholder.png
            Image(
                painter = painterResource(id = R.drawable.personaje_placeholder),
                contentDescription = "Imagen del personaje",
                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // ⭐ Información básica del personaje
            Column {

                // ⭐ Nombre del personaje
                Text(
                    text = personaje.nombre,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ⭐ Nivel y raza
                Text(
                    text = "Nivel 1 • ${personaje.raza}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                // ⭐ Clase del personaje
                Text(
                    text = personaje.clase,
                    color = Color(0xFF7EA7FF),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}