package com.example.rolcraft.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// MODO OSCURO
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7EA7FF),
    secondary = Color(0xFF1B2733),
    tertiary = Color(0xFF7EA7FF),

    background = Color(0xFF0F1720),
    surface = Color(0xFF1B2733),

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// MODO CLARO
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4A6FD6),
    secondary = Color(0xFFE3EAF5),
    tertiary = Color(0xFF4A6FD6),

    background = Color(0xFFF5F7FA),
    surface = Color(0xFFFFFFFF),

    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun RolCraftTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}