package com.example.rolcraft.Listado

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rolcraft.R
import java.text.Normalizer

//Modelo
data class Habilidad(
    val nombre: String,
    val descripcion: String,
    val imagen: Int
)

//Función para ignorar tildes
fun normalizar(texto: String): String {
    return Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .lowercase()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PantallaHabilidades(
    onVolver: () -> Unit
) {

    var busqueda by remember { mutableStateOf("") }
    var habilidadSeleccionada by remember { mutableStateOf<Habilidad?>(null) }

    val habilidades = listOf(
        Habilidad("Ácido", "Daño corrosivo.", R.drawable.acido_resized),
        Habilidad("Agua", "Ataque de agua.", R.drawable.agua_resized),
        Habilidad("Contundente", "Golpe físico pesado.", R.drawable.contundente_resized),
        Habilidad("Cortante", "Daño de corte.", R.drawable.cortante_resized),
        Habilidad("Fuerza", "Energía mágica pura.", R.drawable.fuerza_resized),
        Habilidad("Hielo", "Daño de frío.", R.drawable.hielo_resized),
        Habilidad("Necrótico", "Energía oscura.", R.drawable.necrotico_resized),
        Habilidad("Perforante", "Ataque punzante.", R.drawable.perforante_resized),
        Habilidad("Psíquico", "Ataque mental.", R.drawable.psiquico_resized),
        Habilidad("Radiante", "Luz divina.", R.drawable.radiante_resized),
        Habilidad("Relámpago", "Descarga eléctrica.", R.drawable.relampago_resized),
        Habilidad("Trueno", "Onda sonora.", R.drawable.trueno_resized),
        Habilidad("Veneno", "Daño progresivo.", R.drawable.veneno_resized)
    )

    // FILTRO por inicio + sin tildes
    val filtrados = habilidades.filter {
        normalizar(it.nombre).startsWith(normalizar(busqueda))
    }

    Scaffold(
        containerColor = Color(0xFF0F1720),

        // TOP BAR
        topBar = {
            TopAppBar(
                title = { Text("Habilidades") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F1720))
                .padding(padding)
                .padding(16.dp)
        ) {

            // BUSCADOR
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar habilidad...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // GRID
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtrados) { habilidad ->
                    ItemHabilidad(habilidad) {
                        habilidadSeleccionada = habilidad
                    }
                }
            }
        }
    }

    // POPUP ANIMADO
    habilidadSeleccionada?.let { habilidad ->
        Dialog(onDismissRequest = { habilidadSeleccionada = null }) {

            AnimatedVisibility(
                visible = true,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1B2733)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = habilidad.imagen),
                            contentDescription = habilidad.nombre,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = habilidad.nombre,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = habilidad.descripcion,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ITEM
@Composable
fun ItemHabilidad(
    habilidad: Habilidad,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B2733)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = habilidad.imagen),
                contentDescription = habilidad.nombre,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = habilidad.nombre,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}