package com.ssafy.spico.domain.user.service.auth

import com.ssafy.spico.domain.user.dto.KakaoOAuth2Response
import com.ssafy.spico.domain.user.exception.auth.AuthError
import com.ssafy.spico.domain.user.exception.auth.AuthException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class KakaoOAuth2ServiceImpl(
    private val webClientBuilder: WebClient.Builder
) : KakaoOAuth2Service {

    override fun getUserInfo(accessToken: String): KakaoOAuth2Response {
        return webClientBuilder.build()
            .get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .onStatus({ it.is4xxClientError }) {
                Mono.error(AuthException(AuthError.INVALID_KAKAO_TOKEN))
            }
            .onStatus({ it.is5xxServerError }) {
                Mono.error(AuthException(AuthError.KAKAO_SERVER_ERROR))
            }
            .bodyToMono(KakaoOAuth2Response::class.java)
            .block() ?: throw AuthException(AuthError.INVALID_KAKAO_RESPONSE)
    }
}