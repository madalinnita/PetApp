package com.nitaioanmadalin.petapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String
)
