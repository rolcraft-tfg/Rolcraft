package com.example.rolcraft.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PersonajeDao {

    // ⭐ Guardar personaje
    @Insert
    suspend fun insertarPersonaje(personaje: PersonajeEntity)

    // ⭐ Obtener los personajes del usuario actual
    @Query("SELECT * FROM personajes WHERE usuarioId = :usuarioId")
    suspend fun obtenerPersonajesDeUsuario(usuarioId: String): List<PersonajeEntity>

    // ⭐ Eliminar personaje
    @Query("DELETE FROM personajes WHERE nombre = :nombre")
    suspend fun eliminarPersonaje(nombre: String)
}