package com.example.rolcraft.Data.Repository

import com.example.rolcraft.Data.Local.PersonajeDao
import com.example.rolcraft.Data.Local.PersonajeEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PersonajeRepository(
    private val dao: PersonajeDao
) {

    private val db = FirebaseFirestore.getInstance()

    //insertar personaje

    suspend fun insertarPersonaje(personaje: PersonajeEntity) {

        dao.insertarPersonaje(personaje)

        db.collection("usuarios")
            .document(personaje.usuarioId)
            .collection("personajes")
            .add(personaje)
            .await()
    }

    //obtener personajes

    suspend fun obtenerPersonajes(usuarioId: String): List<PersonajeEntity> {

        val snapshot = db.collection("usuarios")
            .document(usuarioId)
            .collection("personajes")
            .get()
            .await()

        return snapshot.documents.mapNotNull {

            it.toObject(PersonajeEntity::class.java)
        }
    }

    //actualizar personaje

    suspend fun actualizarPersonaje(personaje: PersonajeEntity) {

        dao.actualizarPersonaje(personaje)
    }

    //eliminar personaje

    suspend fun eliminarPersonaje(id: Int) {

        dao.eliminarPersonaje(id)
    }

    //obtener personaje por id

    suspend fun obtenerPersonajePorId(id: Int): PersonajeEntity? {

        return dao.obtenerPersonajePorId(id)
    }
}