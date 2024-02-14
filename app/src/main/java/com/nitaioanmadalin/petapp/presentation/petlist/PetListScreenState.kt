package com.nitaioanmadalin.petapp.presentation.petlist

import com.nitaioanmadalin.petapp.domain.model.Pet


sealed class PetListScreenState {
    data class Loading(
        val daoRepositories: List<Pet>? = null
    ) : PetListScreenState()

    data class Success(val repositories: List<Pet>) : PetListScreenState()

    data class Error(
        val ex: Throwable,
        val isInternetAvailable: Boolean = true
    ) : PetListScreenState()
}