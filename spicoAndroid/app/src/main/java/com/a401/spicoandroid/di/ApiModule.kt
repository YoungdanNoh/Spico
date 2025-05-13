package com.a401.spicoandroid.di

import com.a401.spicoandroid.data.project.api.ProjectApi
import com.a401.spicoandroid.data.practice.api.PracticeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideProjectApi(@NetworkModule.MainRetrofit retrofit: Retrofit)
            : ProjectApi {
        return retrofit.create(ProjectApi::class.java)
    }

    @Provides
    @Singleton
    fun providePracticeApi(@NetworkModule.MainRetrofit retrofit: Retrofit): PracticeApi {
        return retrofit.create(PracticeApi::class.java)
    }

}