package com.nitaioanmadalin.petapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Breed(
    val id : Int = -1,
    val primary: String,
    val secondary: String? = null,
    val mixed: Boolean,
    val unknown: Boolean

): Parcelable