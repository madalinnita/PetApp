package com.nitaioanmadalin.petapp.domain.repository

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.domain.model.Pet
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    fun getAnimals(): Flow<AppResult<List<Pet>>>
    fun getRxAnimals(): Observable<AppResult<List<Pet>>>
}