package com.nitaioanmadalin.petapp.data.repository

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.data.local.PetDao
import com.nitaioanmadalin.petapp.data.local.entities.PetEntity
import com.nitaioanmadalin.petapp.data.remote.api.PetApi
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PetRepositoryImpl(
    private val petDao: PetDao,
    private val petApi: PetApi
) : PetRepository {

    override fun getAnimals(type: String, page: Int): Flow<AppResult<List<Pet>>> = flow {
        emit(AppResult.Loading())
        val cachedPets = petDao.getPets()
        emit(AppResult.Loading(cachedPets.map { it.toPet() }))

        try {
            val animalsDto = petApi.getAnimals(type, page).animals
            animalsDto?.filterNotNull()?.map {
                it.toPetEntity()
            }?.let {
                petDao.insertPets(it)
            }
            val newPets = petDao.getPets().map { it.toPet() }
            emit(AppResult.Success(newPets))
        } catch (ex: Throwable) {
            emit(AppResult.Error(ex, ex.message))
        }
    }
}
