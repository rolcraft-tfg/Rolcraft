package com.example.rolcraft.CrearPersonaje

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.rolcraft.R

// Funcion para las imagenes de las clases
fun obtenerImagenClase(clase: String): Int {
    return when (clase.lowercase().trim()) {
        "mago" -> R.drawable.mago
        "clérigo", "clerigo" -> R.drawable.clerigo
        "guerrero" -> R.drawable.guerrero
        "pícaro", "picaro" -> R.drawable.picaro
        else -> R.drawable.personaje_placeholder
    }
}

// Función para los colores de las clases
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

    // Valores posibles para atributos
    val valoresIniciales = listOf<Int?>(null, 15, 14, 13, 12, 10, 8)

    // Atributos actuales desde el ViewModel
    val fuerza = pj.fuerza
    val destreza = pj.destreza
    val constitucion = pj.constitucion
    val inteligencia = pj.inteligencia
    val sabiduria = pj.sabiduria
    val carisma = pj.carisma

    // Valores ya usados
    val usados = listOf(fuerza, destreza, constitucion, inteligencia, sabiduria, carisma)
    val valoresDisponibles = valoresIniciales.filter { it !in usados }

    // Función simple para actualizar atributos sin duplicar estados
    fun actualizarAtributos(
        fuerza: Int? = pj.fuerza,
        destreza: Int? = pj.destreza,
        constitucion: Int? = pj.constitucion,
        inteligencia: Int? = pj.inteligencia,
        sabiduria: Int? = pj.sabiduria,
        carisma: Int? = pj.carisma
    ) {
        viewModel.actualizarAtributos(
            fuerza,
            destreza,
            constitucion,
            inteligencia,
            sabiduria,
            carisma
        )
        viewModel.aplicarEstadisticas()
    }

    // Validación de atributos
    fun atributoFaltante(): String? {
        return when {
            fuerza == null -> "Fuerza"
            destreza == null -> "Destreza"
            constitucion == null -> "Constitución"
            inteligencia == null -> "Inteligencia"
            sabiduria == null -> "Sabiduría"
            carisma == null -> "Carisma"
            else -> null
        }
    }

    var campoFaltante by remember { mutableStateOf<String?>(null) }

    if (campoFaltante != null) {
        AlertDialog(
            onDismissRequest = { campoFaltante = null },
            title = { Text("Falta un campo") },
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
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        Spacer(Modifier.height(8.dp))

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

                Spacer(Modifier.height(8.dp))

                Button(onClick = { viewModel.generarAleatorio() }) {
                    Text("Aleatorio")
                }
            }

            Card(
                modifier = Modifier.size(100.dp),
                border = BorderStroke(2.dp, obtenerColorClase(pj.clase)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Image(
                    painter = painterResource(id = obtenerImagenClase(pj.clase)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Nombre
        TextField(
            value = pj.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Desplegables
        Desplegable("Género", listaGeneros, pj.genero, null) {
            viewModel.actualizarGenero(it)
        }

        Spacer(Modifier.height(16.dp))

        Desplegable("Raza", listaRazas, pj.raza, descripcionRaza[pj.raza]) {
            viewModel.actualizarRaza(it)
        }

        Spacer(Modifier.height(16.dp))

        Desplegable("Clase", listaClases, pj.clase, descripcionClase[pj.clase]) {
            viewModel.actualizarClase(it)
        }

        Spacer(Modifier.height(16.dp))

        Desplegable(
            "Subclase",
            listaSubclases[pj.clase] ?: emptyList(),
            pj.subclase,
            descripcionSubclase[pj.subclase]
        ) {
            viewModel.actualizarSubclase(it)
        }

        Spacer(Modifier.height(16.dp))

        Desplegable("Trasfondo", listaTrasfondos, pj.trasfondo, descripcionTrasfondo[pj.trasfondo]) {
            viewModel.actualizarTrasfondo(it)
        }

        Spacer(Modifier.height(16.dp))

        Desplegable("Alineamiento", listaAlineamientos, pj.alineamiento, descripcionAlineamiento[pj.alineamiento]) {
            viewModel.actualizarAlineamiento(it)
        }

        Spacer(Modifier.height(24.dp))

        // Atributos
        Text("Atributos", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        SelectorHabilidad("Fuerza", fuerza, valoresDisponibles) { nuevo ->
            actualizarAtributos(fuerza = nuevo)
        }

        SelectorHabilidad("Destreza", destreza, valoresDisponibles) { nuevo ->
            actualizarAtributos(destreza = nuevo)
        }

        SelectorHabilidad("Constitución", constitucion, valoresDisponibles) { nuevo ->
            actualizarAtributos(constitucion = nuevo)
        }

        SelectorHabilidad("Inteligencia", inteligencia, valoresDisponibles) { nuevo ->
            actualizarAtributos(inteligencia = nuevo)
        }

        SelectorHabilidad("Sabiduría", sabiduria, valoresDisponibles) { nuevo ->
            actualizarAtributos(sabiduria = nuevo)
        }

        SelectorHabilidad("Carisma", carisma, valoresDisponibles) { nuevo ->
            actualizarAtributos(carisma = nuevo)
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                val falta = viewModel.validarCampos()
                if (falta != null) {
                    campoFaltante = falta
                    return@Button
                }

                val atributo = atributoFaltante()
                if (atributo != null) {
                    campoFaltante = "Falta elegir $atributo"
                    return@Button
                }

                viewModel.aplicarEstadisticas()
                onSiguiente()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (modoEdicion) "Guardar cambios" else "Ver ficha")
        }
    }
}



@Composable
fun SelectorHabilidad(
    nombre: String,
    valorActual: Int?,
    valoresDisponibles: List<Int?>,
    onValorSeleccionado: (Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(nombre, color = MaterialTheme.colorScheme.onBackground)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(12.dp)
        ) {
            Text(
                text = valorActual?.toString() ?: "–",
                color = MaterialTheme.colorScheme.onSurface
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                valoresDisponibles.forEach { valor ->
                    DropdownMenuItem(
                        text = { Text(valor?.toString() ?: "–") },
                        onClick = {
                            expanded = false
                            onValorSeleccionado(valor)
                        }
                    )
                }
            }
        }
    }
}