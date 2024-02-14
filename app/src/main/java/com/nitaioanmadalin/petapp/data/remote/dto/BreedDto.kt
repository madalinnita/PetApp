package com.nitaioanmadalin.petapp.data.remote.dto

import com.nitaioanmadalin.petapp.data.local.entities.BreedEntity

data class BreedDto(
    val id : Int = -1,
    val primary : String,
    val secondary : String? = null,
    val mixed : Boolean,
    val unknown : Boolean
) {

   fun toBreedEntity()  = BreedEntity(
       id = id,
       primary = primary,
       secondary = secondary,
       mixed = mixed,
       unknown = unknown
   )

}