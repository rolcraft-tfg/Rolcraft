package com.example.rolcraft.CrearPersonaje

data class Personaje(
    val id : Int = 0,
    val nombre: String = "",
    val genero: String = "",
    val raza: String = "",
    val clase: String = "",
    val subclase: String = "",
    val trasfondo: String = "",
    val alineamiento: String = ""
)