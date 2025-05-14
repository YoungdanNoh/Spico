package com.ssafy.spico.domain.user.exception.auth

class AuthException(val error : AuthError) : RuntimeException(error.errorMsg) {
}