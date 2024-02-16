package com.nitaioanmadalin.petapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val id : Int = -1,
    val name: String,
    val breed: Breed? = null,
    val size: String? = null,
    val gender: String? = null,
    val status: String? = null,
    val distance: Double? = null,
    val largeImageUrl: String? = null
): Parcelable