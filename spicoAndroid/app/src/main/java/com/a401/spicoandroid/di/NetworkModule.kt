package com.a401.spicoandroid.di

import android.content.Context
import com.a401.spicoandroid.BuildConfig
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import com.a401.spicoandroid.infrastructure.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainRetrofit

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS) // 서버 연결 시도 제한 시간
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)    // 응답 기다리는 최대 시간
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)   // 요청 전송 시간 제한
            .retryOnConnectionFailure(true)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @MainRetrofit
    @Provides
    @Singleton
    fun provideMainRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // 빌드 시점에 baseUrl 설정
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }
}