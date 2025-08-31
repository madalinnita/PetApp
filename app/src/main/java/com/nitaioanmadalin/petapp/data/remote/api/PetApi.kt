package com.nitaioanmadalin.petapp.data.remote.api

import com.nitaioanmadalin.petapp.data.remote.response.PetResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PetApi {

    @GET("/v2/animals")
    suspend fun getAnimals(
        @Query("type") type: String,
        @Query("page") page: Int
    ): PetResponse

    companion object {
        const val BASE_URL = "https://api.petfinder.com"
    }
}