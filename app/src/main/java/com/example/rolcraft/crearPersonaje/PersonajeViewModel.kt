package com.example.rolcraft.CrearPersonaje

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PersonajeViewModel : ViewModel() {

    // ⭐ Personaje que se está creando actualmente
    var personaje by mutableStateOf(Personaje())
        private set

    // ⭐ Lista donde se guardan todos los personajes creados
    val personajesGuardados = mutableStateListOf<Personaje>()

    // ⭐ Función interna para actualizar el personaje actual
    private fun actualizar(update: Personaje.() -> Personaje) {
        personaje = personaje.update()
    }

    // ⭐ Actualizar nombre
    fun actualizarNombre(n: String) =
        actualizar { copy(nombre = n) }

    // ⭐ Actualizar género
    fun actualizarGenero(g: String) =
        actualizar { copy(genero = g) }

    // ⭐ Actualizar raza
    fun actualizarRaza(r: String) =
        actualizar { copy(raza = r) }

    // ⭐ Actualizar clase
    // Cuando cambia la clase, la subclase se reinicia
    fun actualizarClase(c: String) =
        actualizar { copy(clase = c, subclase = "") }

    // ⭐ Actualizar subclase
    fun actualizarSubclase(s: String) =
        actualizar { copy(subclase = s) }

    // ⭐ Actualizar trasfondo
    fun actualizarTrasfondo(t: String) =
        actualizar { copy(trasfondo = t) }

    // ⭐ Actualizar alineamiento
    fun actualizarAlineamiento(a: String) =
        actualizar { copy(alineamiento = a) }

    // ⭐ Genera un personaje aleatorio
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

    // ⭐ Guarda el personaje actual en la lista de personajes creados
    fun guardarPersonaje() {

        personajesGuardados.add(personaje.copy())

        println("Guardado: $personaje")
    }

    // ⭐ Comprueba si falta algún campo obligatorio
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

    // ⭐ Reinicia el personaje actual para crear uno nuevo
    fun resetearPersonaje() {
        personaje = Personaje()
    }
}