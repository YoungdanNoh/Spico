package com.ssafy.spico.domain.user.controller

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.user.dto.KakaoTokenRequest
import com.ssafy.spico.domain.user.dto.TokenResponse
import com.ssafy.spico.domain.user.service.auth.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService : AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody request : KakaoTokenRequest)
    : ApiResponse<TokenResponse> {
        val response = authService.loginWithKakao(request.kakaoToken)
        return ApiResponse.success(response)
    }
}