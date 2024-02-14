package com.nitaioanmadalin.petapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nitaioanmadalin.petapp.domain.model.Breed

@Entity
data class BreedEntity(
    @PrimaryKey
    val id : Int = -1,
    val primary: String,
    val secondary: String? = null,
    val mixed: Boolean,
    val unknown: Boolean
) {

    fun toBreed(): Breed = Breed(id,primary,secondary,mixed,unknown)

}