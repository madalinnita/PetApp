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

        if (accessToken == null) {
            obtainAccessToken()
        }

        Log.d(TAG, "Access Token: $accessToken")

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        Log.d(TAG, "New Request: $newRequest")

        return chain.proceed(newRequest)
    }

    private fun obtainAccessToken() {
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

        if (response.isSuccessful) {
            val jsonResponse = response.body?.string()
            accessToken = jsonResponse?.let { JSONObject(it).getString("access_token") }
        } else {
            throw SecurityException("Access token could not be retrieved!")
        }
    }

    companion object {
        private const val TAG = "TokenInterceptor"
    }
}