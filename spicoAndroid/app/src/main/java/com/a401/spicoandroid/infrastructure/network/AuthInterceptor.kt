package com.a401.spicoandroid.infrastructure.network
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context,
    private val userDataStore: UserDataStore
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        runBlocking {
            val token = userDataStore.observeAccessToken().first()
            requestBuilder.addHeader("Authorization", "Bearer ${token?.trim()}")
        }

        return chain.proceed(requestBuilder.build())
    }

}