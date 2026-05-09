package com.example.rolcraft.CrearPersonaje

data class Personaje(

    val id : Int = 0,
    val nombre: String = "",
    val genero: String = "",
    val raza: String = "",
    val clase: String = "",
    val subclase: String = "",
    val trasfondo: String = "",
    val alineamiento: String = "",
    val nivel: Int = 1,
    val fuerza: Int? = null,
    val destreza: Int? = null,
    val constitucion: Int? = null,
    val inteligencia: Int? = null,
    val sabiduria: Int? = null,
    val carisma: Int? = null,
    val ac: Int = 10,
    val iniciativa: Int = 0,
    val hp: Int = 1
)