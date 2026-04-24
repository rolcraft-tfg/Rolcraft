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

    // ⭐ UID del usuario autenticado en Firebase
    val usuarioId: String
)