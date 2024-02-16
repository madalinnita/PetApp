package com.nitaioanmadalin.petapp.domain.usecase.getpets

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.domain.model.Pet
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface GetPetsUseCase {
    fun getPetList(): Flow<AppResult<List<Pet>>>
    fun getRxPetList(): Observable<AppResult<List<Pet>>>
}