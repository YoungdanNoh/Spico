package com.a401.spicoandroid.infrastructure.network
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor (
    private val userDataStore: UserDataStore
): Interceptor {

    private val whiteList = listOf("/auth/login")

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val path = request.url.encodedPath

        if (path in whiteList) {
            return chain.proceed(request)
        }

        val requestBuilder = request.newBuilder()

        runBlocking {
            val token = userDataStore.observeAccessToken().first()
            requestBuilder.addHeader("Authorization", "Bearer ${token?.trim()}")
        }

        return chain.proceed(requestBuilder.build())
    }

}