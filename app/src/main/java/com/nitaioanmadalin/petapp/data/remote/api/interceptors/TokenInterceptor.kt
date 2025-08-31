package com.nitaioanmadalin.petapp.data.remote.api.interceptors

import com.nitaioanmadalin.petapp.data.remote.api.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val authApi: AuthApi,
    private val clientId: String,
    private val clientSecret: String
) : Interceptor {

    @Volatile
    private var accessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        accessToken?.let { token ->
            request = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        var response = chain.proceed(request)
        if (response.code == 401 || accessToken == null) {
            synchronized(this) {
                accessToken = runBlocking {
                    authApi.fetchToken(clientId = clientId, clientSecret = clientSecret).accessToken
                }
            }
            response.close()
            val newRequest = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            response = chain.proceed(newRequest)
        }
        return response
    }
}
