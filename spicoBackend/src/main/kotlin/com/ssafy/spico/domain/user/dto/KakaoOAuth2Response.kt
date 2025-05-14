package com.ssafy.spico.domain.user.dto

data class KakaoOAuth2Response(
    val id: Long,
    val properties: Properties
)

data class Properties(
    val nickname: String
)