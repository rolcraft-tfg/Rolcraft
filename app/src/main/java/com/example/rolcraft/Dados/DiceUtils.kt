package com.example.rolcraft.Dados

import android.content.Context

fun obtenerImagenDado(
    context: Context,
    dice: DiceType,
    face: Int,
    tema: DiceTheme
): Int {

    val base = dice.name.trim().lowercase()

    val nombre = when (tema) {
        DiceTheme.AURORA -> "${base}_${face}_aurora"
        DiceTheme.CRYSTAL -> "${base}_${face}_crystal"
    }

    return context.resources.getIdentifier(nombre, "drawable", context.packageName)
}

