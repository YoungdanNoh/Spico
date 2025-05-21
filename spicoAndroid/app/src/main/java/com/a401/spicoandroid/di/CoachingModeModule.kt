package com.a401.spicoandroid.di

import com.a401.spicoandroid.data.coachingmode.api.CoachingModeApi
import com.a401.spicoandroid.data.coachingmode.repository.CoachingModeRepositoryImpl
import com.a401.spicoandroid.domain.coachingmode.repository.CoachingModeRepository
import com.a401.spicoandroid.domain.coachingmode.usecase.PostCoachingResultUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoachingModeModule {

    @Provides
    @Singleton
    fun provideCoachingModeRepository(
        api: CoachingModeApi
    ): CoachingModeRepositoryImpl {
        return CoachingModeRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePostCoachingResultUseCase(
        repository: CoachingModeRepository
    ): PostCoachingResultUseCase {
        return PostCoachingResultUseCase(repository)
    }
}
