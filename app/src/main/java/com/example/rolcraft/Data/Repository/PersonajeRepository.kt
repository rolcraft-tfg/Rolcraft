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

    suspend fun insertarPersonaje(
        personaje: PersonajeEntity
    ) {

        val documento = db.collection("usuarios")
            .document(personaje.usuarioId)
            .collection("personajes")
            .document()

        val personajeFirebase =
            personaje.copy(
                firebaseId = documento.id
            )

        documento
            .set(personajeFirebase)
            .await()

        dao.insertarPersonaje(
            personajeFirebase
        )
    }

    //obtener personajes

    suspend fun obtenerPersonajes(usuarioId: String): List<PersonajeEntity> {

        val snapshot = db.collection("usuarios")
            .document(usuarioId)
            .collection("personajes")
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->

            document.toObject(PersonajeEntity::class.java)
                ?.copy(firebaseId = document.id)
        }
    }

    //sincronizar personajes desde firebase

    suspend fun sincronizarDesdeFirebase(usuarioId: String) {

        val snapshot = db.collection("usuarios")
            .document(usuarioId)
            .collection("personajes")
            .get()
            .await()

        val personajesFirebase = snapshot.documents.mapNotNull { document ->

            document.toObject(PersonajeEntity::class.java)
                ?.copy(firebaseId = document.id)
        }

        personajesFirebase.forEach {

            dao.insertarPersonaje(it)
        }
    }

    //actualizar personaje

    suspend fun actualizarPersonaje(personaje: PersonajeEntity) {

        dao.actualizarPersonaje(personaje)

        db.collection("usuarios")
            .document(personaje.usuarioId)
            .collection("personajes")
            .document(personaje.firebaseId)
            .set(personaje)
            .await()
    }

    //eliminar personaje

    suspend fun eliminarPersonaje(personaje: PersonajeEntity) {

        dao.eliminarPersonaje(personaje.id)

        db.collection("usuarios")
            .document(personaje.usuarioId)
            .collection("personajes")
            .document(personaje.firebaseId)
            .delete()
            .await()
    }

    suspend fun obtenerPersonajePorFirebaseId(
        firebaseId: String
    ): PersonajeEntity? {

        return dao.obtenerPersonajePorFirebaseId(
            firebaseId
        )
    }
}