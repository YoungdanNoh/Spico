package com.ssafy.spico.common.jwt

import io.jsonwebtoken.Claims
import java.nio.charset.StandardCharsets
import javax.crypto.spec.SecretKeySpec
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import io.jsonwebtoken.Jwts
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${JWT_SECRET}") private val secret: String
) {

    private lateinit var secretKey : SecretKey
    private val expiredMs: Long = 60 * 60 * 1000 * 12

    @PostConstruct
    fun initKey() {
        secretKey = SecretKeySpec(
            secret.toByteArray(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().algorithm
        )
    }

    fun parse(token : String) : Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun createJwt(userId : Int) : String {
        val now = Date()
        val exp = Date(now.time + expiredMs)

        return Jwts.builder()
            .claim("userId", userId)
            .claim("role", "USER")
            .issuedAt(now)
            .expiration(exp)
            .signWith(secretKey)
            .compact()
    }

    fun getUserId(token : String) : Int {
        return parse(token).get("userId", Integer::class.java).toInt()
    }

    fun isExpired(token : String) : Boolean {
        return parse(token).expiration.before(Date())
    }

    fun isValidAuthorization(authorizationHeader : String?) : Boolean {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
    }
}