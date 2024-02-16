package com.nitaioanmadalin.petapp.domain.usecase.getpets

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.domain.repository.PetRepository
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

class GetPetsUseCaseImpl(
    val repository: PetRepository
): GetPetsUseCase {
    override fun getPetList(): Flow<AppResult<List<Pet>>> {
        return repository.getAnimals()
    }

    override fun getRxPetList(): Observable<AppResult<List<Pet>>> {
        return repository.getRxAnimals()
    }
}