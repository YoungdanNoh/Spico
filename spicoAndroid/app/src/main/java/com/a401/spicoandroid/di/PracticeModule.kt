package com.a401.spicoandroid.di

import com.a401.spicoandroid.domain.practice.repository.PracticeRepository
import com.a401.spicoandroid.data.practice.repository.PracticeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PracticeModule {
}
