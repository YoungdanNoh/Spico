package com.a401.spicoandroid.data.auth.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.auth.dto.AuthRequest
import com.a401.spicoandroid.data.auth.dto.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun loginWithKakaoToken(
        @Body request: AuthRequest
    ): Response<ApiResponse<AuthResponse>>
}