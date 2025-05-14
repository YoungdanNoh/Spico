package com.ssafy.spico.domain.user.service.auth

import com.ssafy.spico.domain.user.dto.KakaoOAuth2Response

interface KakaoOAuth2Service {
    fun getUserInfo(accessToken: String): KakaoOAuth2Response
}