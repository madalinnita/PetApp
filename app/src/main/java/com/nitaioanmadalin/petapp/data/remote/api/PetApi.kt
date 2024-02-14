package com.nitaioanmadalin.petapp.data.remote.api

import com.nitaioanmadalin.petapp.data.remote.response.PetResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface PetApi {

    @GET("/v2/animals?type=dog&page=2")
   suspend fun getAnimals(): PetResponse

   companion object {
       val BASE_URL = "https://api.petfinder.com"

       val TOKEN_URL = "https://api.petfinder.com/v2/oauth2/token"
   }
}