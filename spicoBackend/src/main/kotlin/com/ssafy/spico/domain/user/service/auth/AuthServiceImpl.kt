package com.ssafy.spico.domain.user.service.auth

import com.ssafy.spico.common.jwt.JwtUtil
import com.ssafy.spico.domain.user.dto.TokenResponse
import com.ssafy.spico.domain.user.entity.UserEntity
import com.ssafy.spico.domain.user.model.toModel
import com.ssafy.spico.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val kakaoOAuth2Service: KakaoOAuth2Service,
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) : AuthService {

    override fun loginWithKakao(kakaoAccessToken: String): TokenResponse {
        val kakaoUser = kakaoOAuth2Service.getUserInfo(kakaoAccessToken)

        val user = userRepository.findByKakaoId(kakaoUser.id)?.toModel()
            ?: userRepository.save(
                UserEntity(kakaoUser.id, kakaoUser.properties.nickname)
            ).toModel()

        val token = jwtUtil.createJwt(user.id)

        return TokenResponse(token, user.nickname, 12 * 60 * 60)
    }
}