package com.ssafy.spico.common.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtUtil : JwtUtil
) : OncePerRequestFilter() {

    private val whiteList = listOf("/auth/login", "/actuator/prometheus")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val uri = request.requestURI
        if(uri in whiteList) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")
        if(!jwtUtil.isValidAuthorization(authHeader)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader!!.substring(7)
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response)
            return
        }

        val userId = jwtUtil.getUserId(token)
        request.setAttribute("userId", userId)

        val authentication = UsernamePasswordAuthenticationToken(userId, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

}