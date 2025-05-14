package com.ssafy.spico.domain.user.dto

data class TokenResponse (
    val accessToken: String,
    val nickname: String,
    val expiresIn: Long
)