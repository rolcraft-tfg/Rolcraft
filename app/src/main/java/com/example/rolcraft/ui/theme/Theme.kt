package com.example.rolcraft.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Personalizado modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7EA7FF),      // azul principal (botones, detalles)
    secondary = Color(0xFF1B2733),    // tarjetas
    tertiary = Color(0xFF7EA7FF),

    background = Color(0xFF0F1720),   // fondo general
    surface = Color(0xFF1B2733),      // superficies (cards, inputs)

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun RolCraftTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}