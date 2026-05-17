package com.example.rolcraft.Data.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PersonajeDao {

    // Guardar personaje
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPersonaje(personaje: PersonajeEntity)

    // Obtener los personajes del usuario actual
    @Query("SELECT * FROM personajes WHERE usuarioId = :usuarioId")
    suspend fun obtenerPersonajesDeUsuario(usuarioId: String): List<PersonajeEntity>
    // Actualizar personaje al editarlo
    @Update
    suspend fun actualizarPersonaje(personaje: PersonajeEntity)
    // Obtener personaje por ID
    @Query("SELECT * FROM personajes WHERE id = :id LIMIT 1")
    suspend fun obtenerPersonajePorId(id: Int): PersonajeEntity?

    // Obtener personaje por firebase id
    @Query("SELECT * FROM personajes WHERE firebaseId = :firebaseId LIMIT 1")
    suspend fun obtenerPersonajePorFirebaseId(
        firebaseId: String
    ): PersonajeEntity?

    // Eliminar personaje
    @Query("DELETE FROM personajes WHERE id = :id")
    suspend fun eliminarPersonaje(id: Int)
}