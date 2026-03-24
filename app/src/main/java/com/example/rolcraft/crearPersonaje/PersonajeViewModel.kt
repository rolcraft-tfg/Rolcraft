package com.example.rolcraft.crearPersonaje

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PersonajeViewModel : ViewModel() {

    var personaje by mutableStateOf(Personaje())
        private set

    private fun actualizar(update: Personaje.() -> Personaje) {
        personaje = personaje.update()
    }

    fun actualizarNombre(n: String) =
        actualizar { copy(nombre = n) }

    fun actualizarGenero(g: String) =
        actualizar { copy(genero = g) }

    fun actualizarRaza(r: String) =
        actualizar { copy(raza = r) }

    fun actualizarClase(c: String) =
        actualizar { copy(clase = c, subclase = "") }

    fun actualizarSubclase(s: String) =
        actualizar { copy(subclase = s) }

    fun actualizarTrasfondo(t: String) =
        actualizar { copy(trasfondo = t) }

    fun actualizarAlineamiento(a: String) =
        actualizar { copy(alineamiento = a) }

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

    fun guardarPersonaje() {
        println("Guardado: $personaje")
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
}