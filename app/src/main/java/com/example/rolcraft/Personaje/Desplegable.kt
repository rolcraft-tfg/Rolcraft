package com.example.rolcraft.Personaje

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Desplegable(
    titulo: String,
    opciones: List<String>,
    valor: String,
    descripcion: String?,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {

        // Fila clicable
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Text(
                text = if (valor.isBlank()) titulo else "$titulo: $valor",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null
            )
        }

        // Descripción debajo del valor seleccionado
        if (!descripcion.isNullOrBlank()) {
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }

        // Lista expandida con descripción dentro
        if (expanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                opciones.forEach { opcion ->

                    val desc = when (titulo) {
                        "Raza" -> descripcionRaza[opcion]
                        "Clase" -> descripcionClase[opcion]
                        "Subclase" -> descripcionSubclase[opcion]
                        "Trasfondo" -> descripcionTrasfondo[opcion]
                        "Alineamiento" -> descripcionAlineamiento[opcion]
                        else -> null
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                            .clickable {
                                onSelect(opcion)
                                expanded = false
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {

                            // Nombre de la opción
                            Text(
                                text = opcion,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            // Descripción debajo
                            if (!desc.isNullOrBlank()) {
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
