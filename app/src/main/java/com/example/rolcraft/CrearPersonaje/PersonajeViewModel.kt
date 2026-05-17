package com.example.rolcraft.CrearPersonaje

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rolcraft.Data.Local.PersonajeEntity
import com.example.rolcraft.Data.Repository.PersonajeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PersonajeViewModel(
    private val repository: PersonajeRepository
) : ViewModel() {

    var personaje by mutableStateOf(Personaje())
        private set

    val personajesGuardados = mutableStateListOf<Personaje>()

    var modoEdicion by mutableStateOf(false)
        private set

    private fun actualizar(update: Personaje.() -> Personaje) {
        personaje = personaje.update()
    }

    // ACTUALIZACIONES

    fun actualizarNombre(n: String) = actualizar {
        copy(nombre = n.take(20))
    }

    fun actualizarGenero(g: String) = actualizar {
        copy(genero = g)
    }

    fun actualizarRaza(r: String) = actualizar {
        copy(raza = r)
    }

    fun actualizarClase(c: String) = actualizar {
        copy(clase = c, subclase = "")
    }

    fun actualizarSubclase(s: String) = actualizar {
        copy(subclase = s)
    }

    fun actualizarTrasfondo(t: String) = actualizar {
        copy(trasfondo = t)
    }

    fun actualizarAlineamiento(a: String) = actualizar {
        copy(alineamiento = a)
    }

    // GENERAR

    fun generarAleatorio() {

        val genero = listaGeneros.random()

        val nombre = when (genero) {

            "Masculino" ->
                nombresMasculinos.random()

            "Femenino" ->
                nombresFemeninos.random()

            "No binario" ->
                nombresNoBinarios.random()

            else -> "SinNombre"
        }.take(20)

        val clase = listaClases.random()

        val subclase =
            listaSubclases[clase]
                ?.random()
                .orEmpty()

        val valores =
            listOf(15, 14, 13, 12, 10, 8)
                .shuffled()

        personaje = personaje.copy(
            nombre = nombre,
            genero = genero,
            raza = listaRazas.random(),
            clase = clase,
            subclase = subclase,
            trasfondo = listaTrasfondos.random(),
            alineamiento = listaAlineamientos.random(),
            fuerza = valores[0],
            destreza = valores[1],
            constitucion = valores[2],
            inteligencia = valores[3],
            sabiduria = valores[4],
            carisma = valores[5]
        )

        personaje =
            calcularEstadisticas(personaje)
    }

    //guardar personaje

    fun guardarPersonaje() {

        viewModelScope.launch {

            val pjConStats =
                calcularEstadisticas(personaje)

            val nombreLimpio =
                pjConStats.nombre
                    .trim()
                    .replace(Regex("\\s+"), " ")

            val personajeFinal =
                pjConStats.copy(
                    nombre = nombreLimpio
                )

            if (
                modoEdicion &&
                personajeFinal.firebaseId.isNotBlank()
            ) {

                repository.actualizarPersonaje(
                    personajeFinal.toEntity()
                )

            } else {

                repository.insertarPersonaje(
                    personajeFinal
                        .copy(id = 0)
                        .toEntity()
                )
            }
            if (modoEdicion) {
                // No resetear nada
            } else {
                resetearPersonaje()
            }

            cargarPersonajes()
        }
    }

    // CARGAR LISTA

    fun cargarPersonajes() {

        viewModelScope.launch {

            val usuarioId =
                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid ?: "INVITADO"

            val lista =
                repository.obtenerPersonajes(
                    usuarioId
                )

            personajesGuardados.clear()

            personajesGuardados.addAll(

                lista.map {

                    Personaje(
                        id = it.id,
                        firebaseId = it.firebaseId,
                        nombre = it.nombre,
                        genero = it.genero,
                        raza = it.raza,
                        clase = it.clase,
                        subclase = it.subclase,
                        trasfondo = it.trasfondo,
                        alineamiento = it.alineamiento,
                        nivel = it.nivel,
                        fuerza = it.fuerza,
                        destreza = it.destreza,
                        constitucion = it.constitucion,
                        inteligencia = it.inteligencia,
                        sabiduria = it.sabiduria,
                        carisma = it.carisma,
                        ac = it.ac,
                        iniciativa = it.iniciativa,
                        hp = it.hp
                    )
                }
            )
        }
    }

    //sincronizar personajes desde firebase

    fun sincronizarFirebase() {

        viewModelScope.launch {

            val usuarioId =
                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid ?: return@launch

            repository.sincronizarDesdeFirebase(
                usuarioId
            )

            cargarPersonajes()
        }
    }

    // CARGAR PARA EDITAR

    fun cargarPersonaje(firebaseId: String) {
        viewModelScope.launch {
            val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            val snapshot = FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(usuarioId)
                .collection("personajes")
                .document(firebaseId)
                .get()
                .await()

            val entity = snapshot.toObject(PersonajeEntity::class.java)

            if (entity != null) {
                personaje = Personaje(
                    id = entity.id,
                    firebaseId = entity.firebaseId,
                    usuarioId = entity.usuarioId,
                    nombre = entity.nombre,
                    genero = entity.genero,
                    raza = entity.raza,
                    clase = entity.clase,
                    subclase = entity.subclase,
                    trasfondo = entity.trasfondo,
                    alineamiento = entity.alineamiento,
                    nivel = entity.nivel,
                    fuerza = entity.fuerza,
                    destreza = entity.destreza,
                    constitucion = entity.constitucion,
                    inteligencia = entity.inteligencia,
                    sabiduria = entity.sabiduria,
                    carisma = entity.carisma,
                    ac = entity.ac,
                    iniciativa = entity.iniciativa,
                    hp = entity.hp
                )

                modoEdicion = true
            }
        }
    }

    // Eliminar personaje

    fun eliminarPersonaje(personaje: Personaje) {

        viewModelScope.launch {

            val personajeEliminar =
                personaje.toEntity()

            repository.eliminarPersonaje(
                personajeEliminar
            )

            cargarPersonajes()
        }
    }

    //VALIDAR

    fun validarCampos(): String? {

        val campos = mapOf(

            "Nombre" to personaje.nombre,
            "Género" to personaje.genero,
            "Raza" to personaje.raza,
            "Clase" to personaje.clase,
            "Subclase" to personaje.subclase,
            "Trasfondo" to personaje.trasfondo,
            "Alineamiento" to personaje.alineamiento
        )

        return campos
            .entries
            .firstOrNull {
                it.value.isBlank()
            }
            ?.key
    }

    //RESET

    fun resetearPersonaje() {

        personaje = Personaje()

        modoEdicion = false
    }

    //  ENTITY

    private fun Personaje.toEntity(): PersonajeEntity {

        return PersonajeEntity(

            id = this.id,

            firebaseId = this.firebaseId,

            nombre = this.nombre,

            raza = this.raza,

            clase = this.clase,

            nivel = this.nivel,

            genero = this.genero,

            subclase = this.subclase,

            trasfondo = this.trasfondo,

            alineamiento = this.alineamiento,

            fuerza = this.fuerza ?: 0,

            destreza = this.destreza ?: 0,

            constitucion = this.constitucion ?: 0,

            inteligencia = this.inteligencia ?: 0,

            sabiduria = this.sabiduria ?: 0,

            carisma = this.carisma ?: 0,

            ac = this.ac,

            iniciativa = this.iniciativa,

            hp = this.hp,

            usuarioId =
                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid ?: "INVITADO"
        )
    }

    // ATRIBUTOS

    fun actualizarAtributos(
        fuerza: Int?,
        destreza: Int?,
        constitucion: Int?,
        inteligencia: Int?,
        sabiduria: Int?,
        carisma: Int?
    ) = actualizar {

        copy(
            fuerza = fuerza,
            destreza = destreza,
            constitucion = constitucion,
            inteligencia = inteligencia,
            sabiduria = sabiduria,
            carisma = carisma
        )
    }

    // STATS

    fun calcularEstadisticas(
        personaje: Personaje
    ): Personaje {

        val dex =
            personaje.destreza ?: 10

        val con =
            personaje.constitucion ?: 10

        val modDex =
            (dex - 10) / 2

        val modCon =
            (con - 10) / 2

        val ac =
            10 + modDex

        val iniciativa =
            maxOf(modDex, 0)

        val hpBase = when (
            personaje.clase
        ) {

            "Bárbaro" -> 12

            "Guerrero",
            "Paladin",
            "Explorador" -> 10

            "Clérigo",
            "Pícaro",
            "Bardo",
            "Monje",
            "Druida",
            "Brujo" -> 8

            "Mago",
            "Hechicero" -> 6

            else -> 8
        }

        val hp =
            hpBase + modCon

        return personaje.copy(
            ac = ac,
            iniciativa = iniciativa,
            hp = hp
        )
    }

    fun aplicarEstadisticas() {

        personaje =
            calcularEstadisticas(personaje)
    }

    // DUPLICAR

    fun guardarPersonajeDuplicado(
        personaje: Personaje
    ) {

        viewModelScope.launch {

            val copia =
                personaje.copy(id = 0)

            repository.insertarPersonaje(
                copia.toEntity()
            )

            cargarPersonajes()
        }
    }
}
