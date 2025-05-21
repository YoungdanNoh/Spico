package com.a401.spicoandroid.domain.auth

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.auth.dto.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(kakaoToken: String): Flow<DataResource<AuthResponse>> // TODO: User 객체가 아니더라도 응답에 사용할 객체 구현 필요
    suspend fun logout() : Flow<DataResource<Unit>>
    suspend fun getLoginState(): Flow<DataResource<Boolean>>
    fun observeAccessToken(): Flow<String?>
    fun observeNickname(): Flow<String?>
    fun observeUserInfo(): Flow<AuthResponse>
}