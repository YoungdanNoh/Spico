package com.ssafy.spico.domain.user.service.auth

import com.ssafy.spico.domain.user.dto.TokenResponse

interface AuthService {
    fun loginWithKakao(kakaoAccessToken: String) : TokenResponse
}