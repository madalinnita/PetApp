package com.nitaioanmadalin.petapp.domain.usecase.getpets

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.domain.model.Pet
import kotlinx.coroutines.flow.Flow

interface GetPetsUseCase {
    fun getPetList(type: String, page: Int): Flow<AppResult<List<Pet>>>
}