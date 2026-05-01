package com.example.rolcraft.CrearPersonaje

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rolcraft.Data.Local.PersonajeEntity
import com.example.rolcraft.Data.Repository.PersonajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonajeViewModel(
    private val repository: PersonajeRepository
) : ViewModel() {

    var personaje by mutableStateOf(Personaje())
        private set

    val personajesGuardados = mutableStateListOf<Personaje>()

    var modoEdicion by mutableStateOf(false)
        private set

    private var personajeOriginal: Int? = null

    private val _personajeActual = MutableStateFlow<Personaje?>(null)
    val personajeActual: StateFlow<Personaje?> = _personajeActual
    private fun actualizar(update: Personaje.() -> Personaje) {
        personaje = personaje.update()
    }

    // SOLO LÍMITE
    fun actualizarNombre(n: String) =
        actualizar {
            copy(nombre = n.take(20))
        }

    fun actualizarGenero(g: String) = actualizar { copy(genero = g) }
    fun actualizarRaza(r: String) = actualizar { copy(raza = r) }
    fun actualizarClase(c: String) = actualizar { copy(clase = c, subclase = "") }
    fun actualizarSubclase(s: String) = actualizar { copy(subclase = s) }
    fun actualizarTrasfondo(t: String) = actualizar { copy(trasfondo = t) }
    fun actualizarAlineamiento(a: String) = actualizar { copy(alineamiento = a) }

    fun generarAleatorio() {
        val genero = listaGeneros.random()

        // SOLO LIMITE, SIN TRIM AQUÍ
        val nombre = when (genero) {
            "Masculino" -> nombresMasculinos.random()
            "Femenino" -> nombresFemeninos.random()
            "No binario" -> nombresNoBinarios.random()
            else -> "SinNombre"
        }.take(20)

        val clase = listaClases.random()
        val subclase = listaSubclases[clase]?.random().orEmpty()

        personaje = Personaje(
            nombre = nombre,
            genero = genero,
            raza = listaRazas.random(),
            clase = clase,
            subclase = subclase,
            trasfondo = listaTrasfondos.random(),
            alineamiento = listaAlineamientos.random()
        )
    }

    fun empezarEdicion(pj: Personaje) {
        personaje = pj
        personajeOriginal = pj.id
        modoEdicion = true
    }

    fun guardarPersonaje() {
        viewModelScope.launch {

            // LIMPIEZA SOLO AQUÍ
            val nombreLimpio = personaje.nombre
                .trim()
                .replace(Regex("\\s+"), " ")

            val personajeLimpio = personaje.copy(nombre = nombreLimpio)

            if (modoEdicion) {
                repository.actualizarPersonaje(personajeLimpio.toEntity())
            } else {
                repository.insertarPersonaje(personajeLimpio.toEntity())
            }

            modoEdicion = false
            cargarPersonajes()
        }
    }

    fun insertarPersonaje(personaje: Personaje) {
        viewModelScope.launch {

            val copia = personaje.copy(id = 0)

            repository.insertarPersonaje(copia.toEntity())
            cargarPersonajes()
        }
    }

    fun cargarPersonajes() {
        viewModelScope.launch {
            val lista = repository.obtenerPersonajes("testUser")

            personajesGuardados.clear()

            personajesGuardados.addAll(
                lista.map {
                    Personaje(
                        id = it.id,
                        nombre = it.nombre,
                        raza = it.raza,
                        clase = it.clase
                    )
                }
            )
        }
    }

    fun eliminarPersonaje(personaje: Personaje) {
        viewModelScope.launch {
            repository.eliminarPersonaje(personaje.id)
            cargarPersonajes()
        }
    }

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

        return campos.entries.firstOrNull { it.value.isBlank() }?.key
    }

    fun resetearPersonaje() {
        personaje = Personaje()
        modoEdicion = false
    }

    private fun Personaje.toEntity(): PersonajeEntity {
        return PersonajeEntity(
            id = this.id,
            nombre = this.nombre,
            raza = this.raza,
            clase = this.clase,
            nivel = 1,
            genero = this.genero,
            subclase = this.subclase,
            trasfondo = this.trasfondo,
            alineamiento = this.alineamiento,
            usuarioId = "testUser"
        )
    }

    fun cargarPersonaje(id: Int) {
        viewModelScope.launch {
            val entity = repository.obtenerPersonajePorId(id)
            _personajeActual.value = entity?.let {
                Personaje(
                    id = it.id,
                    nombre = it.nombre,
                    raza = it.raza,
                    clase = it.clase,
                    genero = it.genero,
                    subclase = it.subclase,
                    trasfondo = it.trasfondo,
                    alineamiento = it.alineamiento
                )
            }
        }
    }
}