package com.example.rolcraft.CrearPersonaje

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rolcraft.Data.Local.PersonajeEntity
import com.example.rolcraft.Data.Repository.PersonajeRepository
import kotlinx.coroutines.launch

class PersonajeViewModel(
    private val repository: PersonajeRepository
) : ViewModel() {

    var personaje by mutableStateOf(Personaje())
        private set

    val personajesGuardados = mutableStateListOf<Personaje>()

    private fun actualizar(update: Personaje.() -> Personaje) {
        personaje = personaje.update()
    }

    fun actualizarNombre(n: String) = actualizar { copy(nombre = n) }
    fun actualizarGenero(g: String) = actualizar { copy(genero = g) }
    fun actualizarRaza(r: String) = actualizar { copy(raza = r) }
    fun actualizarClase(c: String) = actualizar { copy(clase = c, subclase = "") }
    fun actualizarSubclase(s: String) = actualizar { copy(subclase = s) }
    fun actualizarTrasfondo(t: String) = actualizar { copy(trasfondo = t) }
    fun actualizarAlineamiento(a: String) = actualizar { copy(alineamiento = a) }

    fun generarAleatorio() {
        val genero = listaGeneros.random()

        val nombre = when (genero) {
            "Masculino" -> nombresMasculinos.random()
            "Femenino" -> nombresFemeninos.random()
            "No binario" -> nombresNoBinarios.random()
            else -> "SinNombre"
        }

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

    // 🔥 GUARDAR EN ROOM
    fun guardarPersonaje() {
        viewModelScope.launch {
            repository.insertarPersonaje(personaje.toEntity())
            cargarPersonajes() // refresca lista automáticamente
        }
    }

    // 🔥 CARGAR DESDE ROOM
    fun cargarPersonajes() {
        viewModelScope.launch {
            val lista = repository.obtenerPersonajes("testUser")

            personajesGuardados.clear()

            personajesGuardados.addAll(
                lista.map {
                    Personaje(
                        nombre = it.nombre,
                        raza = it.raza,
                        clase = it.clase
                    )
                }
            )
        }
    }

    //  ELIMINAR PERSONAJE con emoticono de papelera
    fun eliminarPersonaje(personaje: Personaje) {
        viewModelScope.launch {
            repository.eliminarPersonaje(personaje.nombre)
            cargarPersonajes() //refresca lista
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
    }

    // 🔁 CONVERSIÓN A ENTITY
    private fun Personaje.toEntity(): PersonajeEntity {
        return PersonajeEntity(
            nombre = this.nombre,
            raza = this.raza,
            clase = this.clase,
            nivel = 1,
            usuarioId = "testUser"
        )
    }
}