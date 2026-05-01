package com.example.rolcraft.FichaPersonaje

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import com.example.rolcraft.CrearPersonaje.PersonajeViewModel
import com.example.rolcraft.Dados.DiceAnimatorCompose
import com.example.rolcraft.Dados.DiceLibrary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ---------------------------------------------------------
// ⭐ PANTALLA PRINCIPAL CON PESTAÑAS (Abilities / Dados)
// ---------------------------------------------------------
@Composable
fun PantallaFichaPersonaje(
    onNuevoPersonaje: () -> Unit,
    onGuardar: () -> Unit,
    onAnterior: () -> Boolean,
    viewModel: PersonajeViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("abilities") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1720))
            .padding(16.dp)
    ) {

        // ⭐ Encabezado estilo D&D Beyond
        EncabezadoFicha(viewModel)

        // ⭐ Barra desplegable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF16202A))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when (selectedTab) {
                        "abilities" -> "Abilities, Saves, Senses"
                        "dados" -> "Dados"
                        else -> "Abilities"
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Abilities, Saves, Senses") },
                    onClick = {
                        selectedTab = "abilities"
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Dados") },
                    onClick = {
                        selectedTab = "dados"
                        expanded = false
                    }
                )
            }
        }

        // ⭐ Contenido dinámico
        when (selectedTab) {
            "abilities" -> SeccionHabilidades(viewModel)
            "dados" -> PantallaDadosInterna(onNuevoPersonaje, onGuardar, onAnterior, viewModel)
        }
    }
}

// ---------------------------------------------------------
// ⭐ ENCABEZADO
// ---------------------------------------------------------
@Composable
fun EncabezadoFicha(viewModel: PersonajeViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1B2733))
            .padding(16.dp)
    ) {

        Text(
            text = viewModel.personaje.nombre,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            EncabezadoStat("AC", "13")
            EncabezadoStat("Initiative", "+2")
            EncabezadoStat("HP", "12/12")
        }
    }
}

@Composable
fun EncabezadoStat(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(titulo, color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
        Text(valor, color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
}

// ---------------------------------------------------------
// ⭐ SECCIÓN DE HABILIDADES
// ---------------------------------------------------------
@Composable
fun SeccionHabilidades(viewModel: PersonajeViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Abilities",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        HabilidadItem("Strength", 1)
        HabilidadItem("Dexterity", 1)
        HabilidadItem("Constitution", 1)
        HabilidadItem("Intelligence", 1)
        HabilidadItem("Wisdom", 1)
        HabilidadItem("Charisma", 1)
    }
}

@Composable
fun HabilidadItem(nombre: String, valor: Int) {

    val modificador = (valor - 10) / 2

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2733)),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(nombre, color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    "Mod: ${if (modificador >= 0) "+$modificador" else modificador}",
                    color = Color.LightGray
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = valor.toString(),
                color = Color(0xFF7EA7FF),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------------------------------------------------------
// ⭐ TU PANTALLA ORIGINAL DE DADOS (SIN CAMBIAR NADA)
// ---------------------------------------------------------
@Composable
fun PantallaDadosInterna(
    onNuevoPersonaje: () -> Unit,
    onGuardar: () -> Unit,
    onAnterior: () -> Boolean,
    viewModel: PersonajeViewModel
) {
    val context = LocalContext.current
    val animator = remember { DiceAnimatorCompose(context) }

    val scope = rememberCoroutineScope()

    var imagenActual by remember { mutableStateOf<Int?>(null) }
    var mostrarResultado by remember { mutableStateOf(false) }
    var resultado by remember { mutableStateOf(0) }

    var position by remember { mutableStateOf(Offset(0f, 0f)) }
    var rotation by remember { mutableStateOf(0f) }

    var smoothX by remember { mutableStateOf(0f) }
    var smoothY by remember { mutableStateOf(0f) }
    var smoothRot by remember { mutableStateOf(0f) }

    var volverAlCentro by remember { mutableStateOf(false) }
    var dadoVisible by remember { mutableStateOf(true) }

    var screenWidth by remember { mutableStateOf(0f) }
    var screenHeight by remember { mutableStateOf(0f) }

    var bloqueado by remember { mutableStateOf(false) }

    // ⭐ Animación original
    LaunchedEffect(Unit) {
        while (true) {
            if (volverAlCentro) {
                smoothX += (0f - smoothX) * 0.08f
                smoothY += (0f - smoothY) * 0.08f
                smoothRot += (0f - smoothRot) * 0.08f
            } else {
                smoothX += (position.x - smoothX) * 0.20f
                smoothY += (position.y - smoothY) * 0.20f
                smoothRot += (rotation - smoothRot) * 0.20f
            }
            delay(16)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                screenWidth = it.size.width.toFloat() / 2f
                screenHeight = it.size.height.toFloat() / 2f
            }
    ) {

        // ⭐ Barra superior de dados (sin cambios)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E))
                .padding(8.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            listOf(
                DiceLibrary.D4,
                DiceLibrary.D6,
                DiceLibrary.D8,
                DiceLibrary.D10,
                DiceLibrary.D12,
                DiceLibrary.D20
            ).forEach { dice ->

                val interaction = remember { MutableInteractionSource() }
                val isPressed by interaction.collectIsPressedAsState()
                val backgroundColor =
                    if (isPressed) Color(0xFF444444) else Color(0xFF2A2A2A)

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(backgroundColor, RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    Button(
                        onClick = {
                            bloqueado = true
                            mostrarResultado = false
                            volverAlCentro = false
                            dadoVisible = true

                            scope.launch {
                                animator.animateDice(
                                    dice = dice,
                                    screenWidth = screenWidth,
                                    screenHeight = screenHeight,
                                    onUpdateFace = { imagenActual = it },
                                    onUpdatePosition = { position = it },
                                    onUpdateRotation = { rotation = it },
                                    onFinish = { res ->
                                        resultado = res
                                        launch {
                                            delay(800)
                                            volverAlCentro = true
                                            mostrarResultado = true
                                        }
                                    }
                                )
                            }
                        },
                        enabled = !mostrarResultado,
                        interactionSource = interaction,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = dice.name, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        // ⭐ Dado animado
        if (dadoVisible) {
            imagenActual?.let { img ->
                Image(
                    painter = painterResource(id = img),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            translationX = smoothX
                            translationY = smoothY
                            rotationZ = smoothRot
                        }
                        .align(Alignment.Center)
                        .zIndex(30f),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // ⭐ Resultado
        AnimatedVisibility(
            visible = mostrarResultado,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 180.dp)
                .zIndex(20f)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(0xAA000000), RoundedCornerShape(12.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "El resultado es: $resultado",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    mostrarResultado = false
                    dadoVisible = false
                    volverAlCentro = false
                    bloqueado = false
                }) {
                    Text("Aceptar")
                }
            }
        }

        // ⭐ Capa que bloquea la pantalla
        if (bloqueado) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000))
                    .zIndex(10f)
                    .pointerInput(Unit) {}
            )
        }
    }
}
