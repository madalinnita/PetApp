package com.nitaioanmadalin.petapp.data.repository

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.data.local.PetDao
import com.nitaioanmadalin.petapp.data.remote.api.PetApi
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PetRepositoryImpl(
    private val dao: PetDao,
    private val api: PetApi
) : PetRepository {
    override fun getAnimals(): Flow<AppResult<List<Pet>>> = flow {
        emit(AppResult.Loading())
        val daoRepositories = dao.getPets()
        emit(AppResult.Loading(daoRepositories.map { it.toPet() }))

        try {
            val animalsDto = api.getAnimals().animals
            animalsDto?.filterNotNull()?.map {
                it.toPetEntity()
            }?.let {
                dao.insertPets(it)
            }
            val newPets = dao.getPets().map { it.toPet() }
            emit(AppResult.Success(newPets))
        } catch (ex: Throwable) {
            emit(AppResult.Error(ex, ex.message))
        }
    }
}