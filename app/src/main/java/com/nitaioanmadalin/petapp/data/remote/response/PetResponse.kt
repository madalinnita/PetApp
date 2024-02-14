package com.nitaioanmadalin.petapp.data.remote.response

import com.nitaioanmadalin.petapp.data.remote.dto.PetDto

data class PetResponse(
    val animals: List<PetDto?>?
)