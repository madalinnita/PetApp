package com.nitaioanmadalin.petapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nitaioanmadalin.petapp.data.local.entities.PetEntity

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPets(repositories: List<PetEntity>)

    @Query("SELECT * FROM petentity")
    suspend fun getPets(): List<PetEntity>
}