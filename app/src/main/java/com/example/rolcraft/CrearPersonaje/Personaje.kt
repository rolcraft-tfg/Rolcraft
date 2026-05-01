package com.example.rolcraft.CrearPersonaje

import androidx.room.PrimaryKey

data class Personaje(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val nombre: String = "",
    val genero: String = "",
    val raza: String = "",
    val clase: String = "",
    val subclase: String = "",
    val trasfondo: String = "",
    val alineamiento: String = ""
)