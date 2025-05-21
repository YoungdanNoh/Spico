package com.a401.spicoandroid.data.auth.dto

data class AuthResponse (
    val accessToken: String,
    val nickname: String,
    val expiresIn: Long
)
