package com.a401.spicoandroid.di

import com.a401.spicoandroid.data.finalmode.repository.FinalModeRepositoryImpl
import com.a401.spicoandroid.domain.finalmode.repository.FinalModeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class FinalModeModule {

    @Binds
    abstract fun bindFinalModeRepository(
        impl: FinalModeRepositoryImpl
    ): FinalModeRepository
}
