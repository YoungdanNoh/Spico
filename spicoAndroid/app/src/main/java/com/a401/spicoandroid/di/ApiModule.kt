package com.a401.spicoandroid.di

import com.a401.spicoandroid.data.main.api.MainApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideMainApi(@NetworkModule.MainRetrofit retrofit: Retrofit)
            : MainApi {
        return retrofit.create(MainApi::class.java)
    }

}