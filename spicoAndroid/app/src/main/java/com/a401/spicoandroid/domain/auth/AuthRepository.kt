package com.a401.spicoandroid.domain.auth

import com.a401.spicoandroid.common.domain.DataResource
import com.microsoft.cognitiveservices.speech.transcription.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(token: String): Flow<DataResource<User>> // TODO: User 객체가 아니더라도 응답에 사용할 객체 구현 필요
    fun logout() : Flow<DataResource<Unit>>

    fun getLoginState(): Flow<DataResource<Boolean>>
    suspend fun saveAccessToken(accessToken: String)
    fun observeAccessToken(): Flow<String?>
}