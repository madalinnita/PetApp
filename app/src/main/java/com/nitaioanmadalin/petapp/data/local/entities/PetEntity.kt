package com.nitaioanmadalin.petapp.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nitaioanmadalin.petapp.domain.model.Pet

@Entity
data class PetEntity(
    @PrimaryKey val id: Int = -1,
    val name: String,

    @Embedded("pet_")
    val breed: BreedEntity? = null,

    val size: String? = null,
    val gender: String? = null,
    val status: String? = null,
    val distance: Double? = null,
    val largeImageUrl: String? = null
) {
    fun toPet(): Pet = Pet(
        id = this.id,
        name = name,
        breed = breed?.toBreed(),
        size = size,
        gender = gender,
        status = status,
        distance = distance,
        largeImageUrl = largeImageUrl
    )
}