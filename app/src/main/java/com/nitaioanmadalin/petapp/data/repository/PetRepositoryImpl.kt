package com.nitaioanmadalin.petapp.data.repository

import com.nitaioanmadalin.petapp.core.utils.network.AppResult
import com.nitaioanmadalin.petapp.data.local.PetDao
import com.nitaioanmadalin.petapp.data.local.entities.PetEntity
import com.nitaioanmadalin.petapp.data.remote.api.PetApi
import com.nitaioanmadalin.petapp.data.remote.dto.PetDto
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.domain.repository.PetRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PetRepositoryImpl(
    private val petDao: PetDao, private val petApi: PetApi
) : PetRepository {
    override fun getAnimals(): Flow<AppResult<List<Pet>>> = flow {
        emit(AppResult.Loading())
        val daoRepositories = petDao.getPets()
        emit(AppResult.Loading(daoRepositories.map { it.toPet() }))

        try {
            val animalsDto = petApi.getAnimals().animals
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

    override fun getRxAnimals(): Observable<AppResult<List<Pet>>> {
        return petDao.getPetsRx().toObservable()
            .subscribeOn(Schedulers.io())
            .flatMap { pets ->
                val loadingState = createLoadingState(pets)
                val apiObservable = fetchPetsFromApiAndInsertIntoDatabase()
                loadingState.concatWith(apiObservable)
            }
    }

    private fun createLoadingState(pets: List<PetEntity>): Observable<AppResult<List<Pet>>> {
        return Observable.just(AppResult.Loading(pets.map { it.toPet() }))
    }

    private fun fetchPetsFromApiAndInsertIntoDatabase(): Observable<AppResult<List<Pet>>> {
        return petApi.getRxAnimals()
            .flatMap { animalsDto ->
                animalsDto.animals?.let { dtoList ->
                    insertPetsIntoDatabase(dtoList.filterNotNull())
                        .flatMap {
                            fetchPetsFromDatabase()
                                .map { pets ->
                                    AppResult.Success(pets)
                                }
                        }
                } ?: Observable.error<AppResult<List<Pet>>>(NullPointerException("Animals list is null"))
            }
            .onErrorReturn { error ->
                AppResult.Error(error, error.message)
            }
    }

    private fun insertPetsIntoDatabase(pets: List<PetDto>): Observable<Unit> {
        return petDao.insertPetsRx(pets.map { it.toPetEntity() }).toObservable()
    }

    private fun fetchPetsFromDatabase(): Observable<List<Pet>> {
        return petDao.getPetsRx().map { pets ->
            pets.map { it.toPet() }
        }.toObservable()
    }
}
