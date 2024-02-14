package com.nitaioanmadalin.petapp.domain.usecase.getdevices

import com.nitaioanmadalin.cosmodeviceexplorer.domain.usecase.getdevices.GetPetsUseCase
import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow

class GetPetsUseCaseImpl(
    val repository: PetRepository
): GetPetsUseCase {
    override fun getCosmoDevices(): Flow<AppResult<List<Pet>>> {
        return repository.getAnimals()
    }
}