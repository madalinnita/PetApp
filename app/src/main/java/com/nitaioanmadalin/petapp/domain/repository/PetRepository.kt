package com.nitaioanmadalin.petapp.domain.repository

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.domain.model.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    fun getAnimals(): Flow<AppResult<List<Pet>>>
}