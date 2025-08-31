package com.nitaioanmadalin.petapp.domain.usecase.getpets

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow

class GetPetsUseCaseImpl(
    val repository: PetRepository
) : GetPetsUseCase {
    override fun getPetList(type: String, page: Int): Flow<AppResult<List<Pet>>> {
        return repository.getAnimals(type, page)
    }
}