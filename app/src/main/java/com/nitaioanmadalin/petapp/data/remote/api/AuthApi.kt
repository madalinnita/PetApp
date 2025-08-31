package com.nitaioanmadalin.petapp.data.remote.api

import com.nitaioanmadalin.petapp.data.remote.response.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {
    @FormUrlEncoded
    @POST("/v2/oauth2/token")
    suspend fun fetchToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): TokenResponse
}
