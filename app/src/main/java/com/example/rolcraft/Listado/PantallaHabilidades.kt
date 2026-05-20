package com.example.rolcraft.Listado

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rolcraft.R
import java.text.Normalizer

data class Habilidad(
    val nombre: String,
    val descripcion: String,
    val imagen: Int
)

fun normalizar(texto: String): String {
    return Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .lowercase()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PantallaHabilidades() {

    var busqueda by remember { mutableStateOf("") }
    var habilidadSeleccionada by remember { mutableStateOf<Habilidad?>(null) }

    val habilidades = listOf(
        Habilidad("Ácido", "Daño corrosivo.", R.drawable.acido),
        Habilidad("Agua", "Ataque de agua.", R.drawable.agua),
        Habilidad("Contundente", "Golpe físico pesado.", R.drawable.contundente),
        Habilidad("Cortante", "Daño de corte.", R.drawable.cortante),
        Habilidad("Fuerza", "Energía mágica pura.", R.drawable.fuerza),
        Habilidad("Fuego", "LLamarada abrasadora.", R.drawable.fuego),
        Habilidad("Hielo", "Daño de frío.", R.drawable.hielo),
        Habilidad("Necrótico", "Energía oscura.", R.drawable.necrotico),
        Habilidad("Perforante", "Ataque punzante.", R.drawable.perforante),
        Habilidad("Psíquico", "Ataque mental.", R.drawable.psiquico),
        Habilidad("Radiante", "Luz divina.", R.drawable.radiante),
        Habilidad("Relámpago", "Descarga eléctrica.", R.drawable.relampago),
        Habilidad("Trueno", "Onda sonora.", R.drawable.trueno),
        Habilidad("Veneno", "Daño progresivo.", R.drawable.veneno)
    )

    val filtrados = habilidades.filter {
        normalizar(it.nombre).startsWith(normalizar(busqueda))
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
                .pointerInput(Unit) {}
        ) {

            Text(
                text = "Habilidades",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador de habilidades
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar habilidad...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

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

    // Popup
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
                        containerColor = MaterialTheme.colorScheme.surface
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
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = habilidad.descripcion,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

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
            containerColor = MaterialTheme.colorScheme.surface
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
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}