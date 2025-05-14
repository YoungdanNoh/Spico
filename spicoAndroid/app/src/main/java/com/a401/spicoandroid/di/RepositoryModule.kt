package com.a401.spicoandroid.di

import com.a401.spicoandroid.data.auth.repository.AuthRepositoryImpl
import com.a401.spicoandroid.data.project.repository.ProjectRepositoryImpl
import com.a401.spicoandroid.data.randomspeech.repository.RandomSpeechRepositoryImpl
import com.a401.spicoandroid.data.report.repository.ReportRepositoryImpl
import com.a401.spicoandroid.domain.auth.AuthRepository
import com.a401.spicoandroid.domain.project.repository.ProjectRepository
import com.a401.spicoandroid.domain.randomspeech.repository.RandomSpeechRepository
import com.a401.spicoandroid.domain.report.repository.ReportRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProjectRepository(
        projectRepositoryImpl: ProjectRepositoryImpl
    ): ProjectRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindRandomSpeechRepository(
        impl: RandomSpeechRepositoryImpl
    ): RandomSpeechRepository
}