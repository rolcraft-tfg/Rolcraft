package com.example.rolcraft.FichaPersonaje

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.zIndex
import com.example.rolcraft.CrearPersonaje.Personaje
import com.example.rolcraft.CrearPersonaje.PersonajeViewModel
import com.example.rolcraft.Dados.DiceAnimatorCompose
import com.example.rolcraft.Dados.DiceLibrary
import com.example.rolcraft.Dados.DiceTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaFichaPersonaje(
    firebaseId: String,
    viewModel: PersonajeViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("características") }

    // Cargar personaje desde Firebase
    LaunchedEffect(firebaseId) {
        viewModel.cargarPersonaje(firebaseId)
    }

    val personaje = viewModel.personaje

    if (viewModel.modoEdicion && personaje.firebaseId.isBlank()) {
        Text("Cargando...", color = MaterialTheme.colorScheme.onBackground)
        return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(LayoutDirection.Ltr),
                    end = padding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = padding.calculateBottomPadding()
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                EncabezadoFicha(personaje)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 0.dp)
                ) {

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
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
                                        "características" -> "Características y Tiradas de Salvación"
                                        "dados" -> "Dados"
                                        "info" -> "Info del Personaje"
                                        else -> "..."
                                    },
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Características y Tiradas de Salvación") },
                                    onClick = {
                                        selectedTab = "características"
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

                                DropdownMenuItem(
                                    text = { Text("Info del Personaje") },
                                    onClick = {
                                        selectedTab = "info"
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    item {
                        when (selectedTab) {
                            "características" -> SeccionHabilidades(personaje)
                            "dados" -> PantallaDadosInterna()
                            "info" -> SeccionInfoPersonaje(personaje)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EncabezadoFicha(personaje: Personaje) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {

        Text(
            text = personaje.nombre,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            EncabezadoStat("AC", personaje.ac.toString())
            EncabezadoStat(
                "Iniciativa",
                if (personaje.iniciativa >= 0) "+${personaje.iniciativa}" else personaje.iniciativa.toString()
            )
            EncabezadoStat("HP", "${personaje.hp}/${personaje.hp}")
        }
    }
}

@Composable
fun EncabezadoStat(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            titulo,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            valor,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun SeccionHabilidades(personaje: Personaje) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Abilities",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        HabilidadItem("Strength", personaje.fuerza)
        HabilidadItem("Dexterity", personaje.destreza)
        HabilidadItem("Constitution", personaje.constitucion)
        HabilidadItem("Intelligence", personaje.inteligencia)
        HabilidadItem("Wisdom", personaje.sabiduria)
        HabilidadItem("Charisma", personaje.carisma)
    }
}

@Composable
fun HabilidadItem(nombre: String, valor: Int?) {

    val modificador = valor?.let { (it - 10) / 2 }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    nombre,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = modificador?.let { if (it >= 0) "+$it" else "$it" } ?: "–",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = valor?.toString() ?: "–",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PantallaDadosInterna() {

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
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
                    if (isPressed) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.surfaceVariant

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
                                    onUpdateFace = { face ->
                                        imagenActual = dice.images[face - 1]
                                    },
                                    onUpdatePosition = { position = it },
                                    onUpdateRotation = { rotation = it },

                                    onFinish = { res ->
                                        resultado = res
                                        launch {
                                            delay(800)
                                            volverAlCentro = true
                                            mostrarResultado = true
                                            bloqueado = false
                                        }
                                    }
                                )
                            }
                        },
                        enabled = !mostrarResultado,
                        interactionSource = interaction,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = dice.name, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        if (dadoVisible) {
            imagenActual?.let { img ->
                if (img != 0) {
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
        }

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
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "El resultado es: $resultado",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    mostrarResultado = false
                    dadoVisible = false
                    volverAlCentro = false
                }) {
                    Text("Aceptar")
                }
            }
        }

        if (bloqueado) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
                    .zIndex(10f)
                    .pointerInput(Unit) {}
            )
        }
    }
}

@Composable
fun SeccionInfoPersonaje(personaje: Personaje) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Información del Personaje",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        InfoItem("Nombre", personaje.nombre)
        InfoItem("Género", personaje.genero)
        InfoItem("Raza", personaje.raza)
        InfoItem("Clase", personaje.clase)
        InfoItem("Subclase", personaje.subclase)
        InfoItem("Trasfondo", personaje.trasfondo)
        InfoItem("Alineamiento", personaje.alineamiento)
    }
}

@Composable
fun InfoItem(titulo: String, valor: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = titulo,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = valor.ifBlank { "—" },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}