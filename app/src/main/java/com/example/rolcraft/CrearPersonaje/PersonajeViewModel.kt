package com.example.rolcraft.CrearPersonaje

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rolcraft.Dados.DiceTheme
import com.example.rolcraft.Data.Local.PersonajeEntity
import com.example.rolcraft.Data.Repository.PersonajeRepository
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

    private fun actualizar(update: Personaje.() -> Personaje) {
        personaje = personaje.update()
    }
    var temaDados by mutableStateOf(DiceTheme.AURORA)
        private set

    // ----------- ACTUALIZACIONES -----------

    fun actualizarNombre(n: String) = actualizar { copy(nombre = n.take(20)) }
    fun actualizarGenero(g: String) = actualizar { copy(genero = g) }
    fun actualizarRaza(r: String) = actualizar { copy(raza = r) }
    fun actualizarClase(c: String) = actualizar { copy(clase = c, subclase = "") }
    fun actualizarSubclase(s: String) = actualizar { copy(subclase = s) }
    fun actualizarTrasfondo(t: String) = actualizar { copy(trasfondo = t) }
    fun actualizarAlineamiento(a: String) = actualizar { copy(alineamiento = a) }

    // ----------- GENERAR -----------

    fun generarAleatorio() {
        val genero = listaGeneros.random()

        val nombre = when (genero) {
            "Masculino" -> nombresMasculinos.random()
            "Femenino" -> nombresFemeninos.random()
            "No binario" -> nombresNoBinarios.random()
            else -> "SinNombre"
        }.take(20)

        val clase = listaClases.random()
        val subclase = listaSubclases[clase]?.random().orEmpty()
        val valores = listOf(15, 14, 13, 12, 10, 8).shuffled()

        personaje = Personaje(
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

        personaje = calcularEstadisticas(personaje)
    }

    // ----------- GUARDAR -----------

    fun guardarPersonaje() {
        viewModelScope.launch {

            val pjConStats = calcularEstadisticas(personaje)

            val nombreLimpio = pjConStats.nombre
                .trim()
                .replace(Regex("\\s+"), " ")

            var personajeFinal = pjConStats.copy(nombre = nombreLimpio)

            if (modoEdicion && personajeOriginal != null) {
                personajeFinal = personajeFinal.copy(id = personajeOriginal!!)
                repository.actualizarPersonaje(personajeFinal.toEntity())
            } else {
                personajeFinal = personajeFinal.copy(id = 0)
                repository.insertarPersonaje(personajeFinal.toEntity())
            }

            resetearPersonaje()
            cargarPersonajes()
        }
    }

    // ----------- CARGAR LISTA -----------

    fun cargarPersonajes() {
        viewModelScope.launch {
            val lista = repository.obtenerPersonajes("testUser")

            personajesGuardados.clear()

            personajesGuardados.addAll(
                lista.map {
                    Personaje(
                        id = it.id,
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

    // ----------- CARGAR PARA EDITAR -----------

    fun cargarPersonaje(id: Int) {
        viewModelScope.launch {
            val entity = repository.obtenerPersonajePorId(id)
            if (entity != null) {
                personaje = Personaje(
                    id = entity.id,
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
                personajeOriginal = entity.id
                modoEdicion = true
            }
        }
    }

    // ----------- OTROS -----------

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
        personajeOriginal = null
    }

    private fun Personaje.toEntity(): PersonajeEntity {
        return PersonajeEntity(
            id = this.id,
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
            usuarioId = "testUser"
        )
    }

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

    fun calcularEstadisticas(personaje: Personaje): Personaje {
        val dex = personaje.destreza ?: 10
        val con = personaje.constitucion ?: 10
        val modDex = (dex - 10) / 2
        val modCon = (con - 10) / 2
        val ac = 10 + modDex
        val iniciativa = maxOf(modDex, 0)

        val hpBase = when (personaje.clase) {
            "Bárbaro" -> 12
            "Guerrero", "Paladin", "Explorador" -> 10
            "Clérigo", "Pícaro", "Bardo", "Monje", "Druida", "Brujo" -> 8
            "Mago", "Hechicero" -> 6
            else -> 8
        }

        val hp = hpBase + modCon

        return personaje.copy(
            ac = ac,
            iniciativa = iniciativa,
            hp = hp
        )
    }

    fun aplicarEstadisticas() {
        personaje = calcularEstadisticas(personaje)
    }

    fun guardarPersonajeDuplicado(personaje: Personaje) {
        viewModelScope.launch {
            val copia = personaje.copy(id = 0)
            repository.insertarPersonaje(copia.toEntity())
            cargarPersonajes()
        }
    }

    fun cambiarTemaDados(nuevoTema: DiceTheme) {
        temaDados = nuevoTema
    }
}
