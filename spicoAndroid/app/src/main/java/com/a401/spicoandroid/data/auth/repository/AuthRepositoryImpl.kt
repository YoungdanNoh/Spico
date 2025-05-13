package com.a401.spicoandroid.data.auth.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.auth.AuthRepository
import com.microsoft.cognitiveservices.speech.transcription.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(

): AuthRepository {
    override fun login(token: String): Flow<DataResource<User>> {
        TODO("Not yet implemented")
    }

    override fun logout(): Flow<DataResource<Unit>> {
        TODO("Not yet implemented")
    }

    override fun getLoginState(): Flow<DataResource<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAccessToken(accessToken: String) {
        TODO("Not yet implemented")
    }

    override fun observeAccessToken(): Flow<String?> {
        TODO("Not yet implemented")
    }

}