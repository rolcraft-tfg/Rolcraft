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

    val valoresIniciales = listOf<Int?>(null, 15, 14, 13, 12, 10, 8)
    var valoresDisponibles by remember { mutableStateOf(valoresIniciales) }

    var fuerza by remember { mutableStateOf<Int?>(null) }
    var destreza by remember { mutableStateOf<Int?>(null) }
    var constitucion by remember { mutableStateOf<Int?>(null) }
    var inteligencia by remember { mutableStateOf<Int?>(null) }
    var sabiduria by remember { mutableStateOf<Int?>(null) }
    var carisma by remember { mutableStateOf<Int?>(null) }

    var campoFaltante by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pj) {
        fuerza = pj.fuerza
        destreza = pj.destreza
        constitucion = pj.constitucion
        inteligencia = pj.inteligencia
        sabiduria = pj.sabiduria
        carisma = pj.carisma

        val usados = listOf(fuerza, destreza, constitucion, inteligencia, sabiduria, carisma)
        valoresDisponibles = valoresIniciales.filter { it !in usados }
    }

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

    //Función para sacar de la lista los valores ya seleccionados de atributos

    fun actualizarValor(
        nuevo: Int?,
        actual: Int?,
        setter: (Int?) -> Unit
    ) {
        if (actual != null) {
            valoresDisponibles = valoresDisponibles + actual
        }

        setter(nuevo)

        if (nuevo != null) {
            valoresDisponibles = valoresDisponibles - nuevo
        }
    }

    //Función para obtener el nombre de un atributo no seleccionado

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

        val nombreState = remember { mutableStateOf(TextFieldValue(pj.nombre)) }

        LaunchedEffect(pj.nombre) {
            if (nombreState.value.text != pj.nombre) {
                nombreState.value = TextFieldValue(pj.nombre)
            }
        }

        TextField(
            value = nombreState.value,
            onValueChange = { nuevo ->
                val texto = nuevo.text.take(20)
                nombreState.value = nuevo.copy(text = texto)
                viewModel.actualizarNombre(texto)
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

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

        Spacer(Modifier.height(16.dp))

        // Selectores de atributos

        Text("Atributos", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(12.dp))

        SelectorHabilidad("Fuerza", fuerza, valoresDisponibles) { nuevo ->
            actualizarValor(nuevo, fuerza) { fuerza = it }
            viewModel.actualizarAtributos(
                fuerza = nuevo,
                destreza = destreza,
                constitucion = constitucion,
                inteligencia = inteligencia,
                sabiduria = sabiduria,
                carisma = carisma
            )
            viewModel.aplicarEstadisticas()
        }

        SelectorHabilidad("Destreza", destreza, valoresDisponibles) { nuevo ->
            actualizarValor(nuevo, destreza) { destreza = it }
            viewModel.actualizarAtributos(
                fuerza = fuerza,
                destreza = nuevo,
                constitucion = constitucion,
                inteligencia = inteligencia,
                sabiduria = sabiduria,
                carisma = carisma
            )
            viewModel.aplicarEstadisticas()
        }

        SelectorHabilidad("Constitución", constitucion, valoresDisponibles) { nuevo ->
            actualizarValor(nuevo, constitucion) { constitucion = it }
            viewModel.actualizarAtributos(
                fuerza = fuerza,
                destreza = destreza,
                constitucion = nuevo,
                inteligencia = inteligencia,
                sabiduria = sabiduria,
                carisma = carisma
            )
            viewModel.aplicarEstadisticas()
        }

        SelectorHabilidad("Inteligencia", inteligencia, valoresDisponibles) { nuevo ->
            actualizarValor(nuevo, inteligencia) { inteligencia = it }
            viewModel.actualizarAtributos(
                fuerza = fuerza,
                destreza = destreza,
                constitucion = constitucion,
                inteligencia = nuevo,
                sabiduria = sabiduria,
                carisma = carisma
            )
            viewModel.aplicarEstadisticas()
        }

        SelectorHabilidad("Sabiduría", sabiduria, valoresDisponibles) { nuevo ->
            actualizarValor(nuevo, sabiduria) { sabiduria = it }
            viewModel.actualizarAtributos(
                fuerza = fuerza,
                destreza = destreza,
                constitucion = constitucion,
                inteligencia = inteligencia,
                sabiduria = nuevo,
                carisma = carisma
            )
            viewModel.aplicarEstadisticas()
        }

        SelectorHabilidad("Carisma", carisma, valoresDisponibles) { nuevo ->
            actualizarValor(nuevo, carisma) { carisma = it }
            viewModel.actualizarAtributos(
                fuerza = fuerza,
                destreza = destreza,
                constitucion = constitucion,
                inteligencia = inteligencia,
                sabiduria = sabiduria,
                carisma = nuevo
            )
            viewModel.aplicarEstadisticas()
        }

        Spacer(Modifier.height(32.dp))

        // Aviso de campo no introducido

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

                viewModel.actualizarAtributos(
                    fuerza!!,
                    destreza!!,
                    constitucion!!,
                    inteligencia!!,
                    sabiduria!!,
                    carisma!!
                )
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