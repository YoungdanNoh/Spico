package com.a401.spicoandroid.data.auth.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.auth.api.AuthApi
import com.a401.spicoandroid.data.auth.dto.AuthRequest
import com.a401.spicoandroid.data.auth.dto.AuthResponse
import com.a401.spicoandroid.domain.auth.AuthRepository
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val userDataStore: UserDataStore
): AuthRepository {
    override suspend fun login(
        kakaoToken: String
    ): Flow<DataResource<AuthResponse>> = flow {
        emit(DataResource.Loading())

        val result = safeApiCall {
            val request = AuthRequest(kakaoToken)
            val response = api.loginWithKakaoToken(request)
            val data = response.body()?.data ?: throw Exception("jwt token 발급 실패")

            userDataStore.setUserInfo(
                data.accessToken,
                data.nickname,
                data.expiresIn
            )
            data
        }
        emit(result)
    }

    override suspend fun logout(): Flow<DataResource<Unit>> = flow {
        emit(DataResource.loading())
        userDataStore.clear()
        emit(DataResource.Success(Unit))
    }

    override suspend fun getLoginState(): Flow<DataResource<Boolean>> = flow {
        emit(DataResource.loading())

        val isLoggedIn = !(userDataStore.isTokenExpired())
        emit(DataResource.Success(isLoggedIn))
    }

    override fun observeAccessToken(): Flow<String?> {
        return userDataStore.accessToken
    }

    override fun observeNickname(): Flow<String?> {
        return userDataStore.nickname
    }

    override fun observeUserInfo(): Flow<AuthResponse> {
        return userDataStore.observeUserInfo()
    }

}