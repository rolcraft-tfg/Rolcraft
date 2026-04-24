package com.example.rolcraft.CrearPersonaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rolcraft.Data.Repository.PersonajeRepository

class PersonajeViewModelFactory(
    private val repository: PersonajeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonajeViewModel::class.java)) {
            return PersonajeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}