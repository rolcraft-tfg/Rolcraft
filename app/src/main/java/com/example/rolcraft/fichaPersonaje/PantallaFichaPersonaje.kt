package com.example.rolcraft

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.zIndex
import com.example.rolcraft.crearPersonaje.PersonajeViewModel
import com.example.rolcraft.dados.DiceAnimatorCompose
import com.example.rolcraft.dados.DiceLibrary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaFichaPersonaje(
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

    // ANIMACIÓN SUAVE CONTINUA
    LaunchedEffect(Unit) {
        while (true) {
            if (volverAlCentro) {
                // fase de regreso al centro
                smoothX += (0f - smoothX) * 0.08f
                smoothY += (0f - smoothY) * 0.08f
                smoothRot += (0f - smoothRot) * 0.08f
            } else {
                // fase de física: seguir la posición/rotación reales
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

        // ----------------------------------------------------
        //  BARRA SUPERIOR CONTINUA CON BOTONES CUADRADOS
        // ----------------------------------------------------
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
                                        // la física ha terminado aquí
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
                        Text(
                            text = dice.name,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // -------------------------
        //  DADO
        // -------------------------
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
                        .zIndex(1f),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // -------------------------
        //  MENSAJE DEBAJO DEL DADO
        // -------------------------
        AnimatedVisibility(
            visible = mostrarResultado,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 180.dp)
                .zIndex(5f)
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
                }) {
                    Text("Aceptar")
                }
            }
        }
    }
}
