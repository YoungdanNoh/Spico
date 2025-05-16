package com.a401.spicoandroid.di

import com.a401.spicoandroid.data.report.api.FinalReportApi
import com.a401.spicoandroid.data.report.repository.FinalReportRepositoryImpl
import com.a401.spicoandroid.domain.report.repository.FinalReportRepository
import com.a401.spicoandroid.domain.report.usecase.GetFinalReportUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FinalReportModule {

    @Provides
    fun provideFinalReportRepository(
        api: FinalReportApi
    ): FinalReportRepository {
        return FinalReportRepositoryImpl(api)
    }

    @Provides
    fun provideGetFinalReportUseCase(
        repository: FinalReportRepository
    ): GetFinalReportUseCase {
        return GetFinalReportUseCase(repository)
    }
}
