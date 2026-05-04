package com.example.rolcraft.Data.Repository

import com.example.rolcraft.Data.Local.PersonajeDao
import com.example.rolcraft.Data.Local.PersonajeEntity

class PersonajeRepository(
    private val dao: PersonajeDao
) {

    suspend fun insertarPersonaje(personaje: PersonajeEntity) {
        dao.insertarPersonaje(personaje)
    }

    suspend fun obtenerPersonajes(usuarioId: String): List<PersonajeEntity> {
        return dao.obtenerPersonajesDeUsuario(usuarioId)
    }

    suspend fun actualizarPersonaje(personaje: PersonajeEntity) {
        dao.actualizarPersonaje(personaje)
        suspend fun eliminarPersonaje(id: Int) {
            dao.eliminarPersonaje(id)
        }
    }
    suspend fun obtenerPersonajePorId(id: Int): PersonajeEntity? {
        return dao.obtenerPersonajePorId(id)
    }

}
