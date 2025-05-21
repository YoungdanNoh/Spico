package com.a401.spicoandroid.di

import android.content.Context
import com.a401.spicoandroid.infrastructure.camera.UploadManager
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
object UploadModule {

    @Provides
    @Singleton
    fun provideUploadManager(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): UploadManager = UploadManager(context, okHttpClient)
}

