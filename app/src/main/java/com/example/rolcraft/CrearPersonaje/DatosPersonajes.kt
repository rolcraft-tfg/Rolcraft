package com.example.rolcraft.CrearPersonaje

val listaGeneros = listOf("Masculino", "Femenino", "No binario")

val nombresMasculinos = listOf(
    "Aldric", "Baldur", "Cedric", "Darian", "Elric",
    "Falken", "Garrick", "Hadrian", "Ivor", "Jarek"
)

val nombresFemeninos = listOf(
    "Ariana", "Brienne", "Celene", "Dalia", "Elowen",
    "Fiora", "Gwen", "Helena", "Isolde", "Jaina"
)

val nombresNoBinarios = listOf(
    "Ash", "Briar", "Cyan", "Drin", "Eris",
    "Fenn", "Gale", "Hale", "Indra", "Jori"
)

val listaRazas = listOf("Humano", "Elfo", "Enano", "Mediano", "Orco")

val descripcionRaza = mapOf(
    "Humano" to "Versátiles y adaptables.",
    "Elfo" to "Ágiles y longevos.",
    "Enano" to "Fuertes y resistentes.",
    "Mediano" to "Sigilosos y afortunados.",
    "Orco" to "Poderosos y feroces."
)

val listaClases = listOf("Guerrero", "Mago", "Pícaro", "Clérigo")

val descripcionClase = mapOf(
    "Guerrero" to "Combatiente experto.",
    "Mago" to "Dominador de la magia.",
    "Pícaro" to "Sigiloso y astuto.",
    "Clérigo" to "Devoto con magia divina."
)

val listaSubclases = mapOf(
    "Guerrero" to listOf("Campeón", "Maestro de batalla"),
    "Mago" to listOf("Evocador", "Ilusionista"),
    "Pícaro" to listOf("Asesino", "Trampero"),
    "Clérigo" to listOf("Vida", "Guerra")
)

val descripcionSubclase = mapOf(
    "Campeón" to "Fuerza bruta y resistencia.",
    "Maestro de batalla" to "Tácticas y control.",
    "Evocador" to "Magia destructiva.",
    "Ilusionista" to "Engaños y trucos.",
    "Asesino" to "Golpes precisos.",
    "Trampero" to "Control del entorno.",
    "Vida" to "Sanación y apoyo.",
    "Guerra" to "Combate divino."
)

val listaTrasfondos = listOf("Acolito", "Criminal", "Erudito", "Soldado")

val descripcionTrasfondo = mapOf(
    "Acolito" to "Criado en un templo.",
    "Criminal" to "Vida entre sombras.",
    "Erudito" to "Dedicado al conocimiento.",
    "Soldado" to "Entrenado para la guerra."
)

val listaAlineamientos = listOf(
    "Legal Bueno", "Neutral Bueno", "Caótico Bueno",
    "Legal Neutral", "Neutral", "Caótico Neutral",
    "Legal Malvado", "Neutral Malvado", "Caótico Malvado"
)

val descripcionAlineamiento = mapOf(
    "Legal Bueno" to "Justo y honorable.",
    "Neutral Bueno" to "Ayuda sin reglas estrictas.",
    "Caótico Bueno" to "Hace el bien a su manera.",
    "Neutral" to "Equilibrado y práctico."
)
