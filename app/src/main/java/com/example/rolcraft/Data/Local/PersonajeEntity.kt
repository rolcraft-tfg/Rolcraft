package com.example.rolcraft.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personajes")
data class PersonajeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val raza: String,
    val clase: String,
    val nivel: Int,
    val genero: String,
    val subclase: String,
    val trasfondo: String,
    val alineamiento: String,
    val fuerza: Int,
    val destreza: Int,
    val constitucion: Int,
    val inteligencia: Int,
    val sabiduria: Int,
    val carisma: Int,
    val ac: Int,
    val iniciativa: Int,
    val hp: Int,
    // ⭐ UID del usuario autenticado en Firebase
    val usuarioId: String
)