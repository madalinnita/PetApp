package com.nitaioanmadalin.petapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nitaioanmadalin.petapp.data.local.entities.BreedEntity
import com.nitaioanmadalin.petapp.data.local.entities.PetEntity

@Database(
    entities = [PetEntity::class, BreedEntity::class],
    version = 1
)
abstract class PetDatabase: RoomDatabase() {
    abstract val dao: PetDao
}