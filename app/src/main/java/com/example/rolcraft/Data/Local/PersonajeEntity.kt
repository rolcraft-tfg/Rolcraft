package com.example.rolcraft.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personajes")
data class PersonajeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String = "",

    val raza: String = "",

    val clase: String = "",

    val nivel: Int = 1,

    val genero: String = "",

    val subclase: String = "",

    val trasfondo: String = "",

    val alineamiento: String = "",

    val fuerza: Int = 0,

    val destreza: Int = 0,

    val constitucion: Int = 0,

    val inteligencia: Int = 0,

    val sabiduria: Int = 0,

    val carisma: Int = 0,

    val ac: Int = 0,

    val iniciativa: Int = 0,

    val hp: Int = 0,

    val usuarioId: String = ""
)