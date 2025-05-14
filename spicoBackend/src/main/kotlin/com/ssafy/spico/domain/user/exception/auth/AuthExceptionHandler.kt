package com.ssafy.spico.domain.user.exception.auth

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.practice.exception.PracticeException
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(2)
class AuthExceptionHandler {

    @ExceptionHandler(AuthException::class)
    fun handleAuthException(exception: PracticeException): ApiResponse<Any?> {
        val error = exception.error

        val clientMsg = if (error.httpStatusCode.startsWith("4") || error.httpStatusCode.startsWith("5")) {
            error.errorMsg
        } else {
            "일시적인 서버 오류가 발생했습니다"
        }

        return ApiResponse.error(error.errorCode, clientMsg)
    }

}