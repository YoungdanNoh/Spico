package com.a401.spicoandroid.infrastructure.network

import android.content.Context
import com.a401.spicoandroid.infrastructure.datastore.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        runBlocking {
            val token = TokenDataStore.getAccessToken(context).first()
            requestBuilder.addHeader("Authorization", "Bearer ${token?.trim()}")
        }

        return chain.proceed(requestBuilder.build())
    }

}