package com.nitaioanmadalin.petapp.data.remote.dto

import com.nitaioanmadalin.petapp.data.local.entities.PetEntity

data class PetDto(
    val id : Int,
    val name: String,
    val breeds: BreedDto? = null,
    val size: String? = null,
    val gender: String? = null,
    val status: String? = null,
    val distance: Double? = null,
) {
    fun toPetEntity() = PetEntity(
        id = this.id,
        name = name,
        breed = breeds?.toBreedEntity(),
        size = size,
        gender = gender,
        status = status,
        distance=distance
    )
}