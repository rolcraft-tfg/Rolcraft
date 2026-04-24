package com.example.rolcraft.Data.Local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        PersonajeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personajeDao(): PersonajeDao
}