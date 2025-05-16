package com.a401.spicoandroid.di

import com.a401.spicoandroid.data.auth.api.AuthApi
import com.a401.spicoandroid.data.finalmode.api.FinalModeApi
import com.a401.spicoandroid.data.home.api.HomeApi
import com.a401.spicoandroid.data.practice.api.PracticeApi
import com.a401.spicoandroid.data.project.api.ProjectApi
import com.a401.spicoandroid.data.randomspeech.api.RandomSpeechApi
import com.a401.spicoandroid.data.report.api.FinalReportApi
import com.a401.spicoandroid.data.report.api.ReportApi
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
    fun provideHomeApi(@NetworkModule.MainRetrofit retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

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

    @Provides
    @Singleton
    fun provideRandomSpeechApi(@NetworkModule.MainRetrofit retrofit: Retrofit): RandomSpeechApi {
        return retrofit.create(RandomSpeechApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReportApi(@NetworkModule.MainRetrofit retrofit: Retrofit): ReportApi {
        return retrofit.create(ReportApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFinalModeApi(@NetworkModule.MainRetrofit retrofit: Retrofit): FinalModeApi {
        return retrofit.create(FinalModeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFinalReportApi(@NetworkModule.MainRetrofit retrofit: Retrofit): FinalReportApi {
        return retrofit.create(FinalReportApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(@NetworkModule.MainRetrofit retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

}