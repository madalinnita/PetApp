package com.nitaioanmadalin.cosmodeviceexplorer.domain.usecase.getdevices

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.domain.model.Pet
import kotlinx.coroutines.flow.Flow

interface GetPetsUseCase {
    fun getCosmoDevices(): Flow<AppResult<List<Pet>>>
}