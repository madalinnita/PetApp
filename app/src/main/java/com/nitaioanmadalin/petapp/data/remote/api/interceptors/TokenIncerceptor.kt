package com.nitaioanmadalin.petapp.data.remote.api.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

class TokenInterceptor(private val tokenUrl: String, private val clientId: String, private val clientSecret: String) :
    Interceptor {

    private var accessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var request = originalRequest

        accessToken?.let { token ->
            request = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        val response = chain.proceed(request)

        if (response.code == 401 || accessToken == null) {
            accessToken = obtainAccessToken()
            if (accessToken != null) {
                response.close()
                request = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
                return chain.proceed(request)
            }
        }

        Log.d(TAG, accessToken.toString())

        return response
    }

    private fun obtainAccessToken(): String? {
        val requestBody = JSONObject()
            .put("grant_type", "client_credentials")
            .put("client_id", clientId)
            .put("client_secret", clientSecret)
            .toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(tokenUrl)
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            val jsonResponse = response.body?.string()
            jsonResponse?.let { JSONObject(it).getString("access_token") }
        } else {
            throw SecurityException("Access token could not be retrieved!")
        }
    }

    companion object {
        private const val TAG = "TokenInterceptor"
    }
}