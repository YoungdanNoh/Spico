package com.ssafy.spico.domain.practice.exception

import com.ssafy.spico.common.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(2)
class PracticeExceptionHandler {

    private val logger = LoggerFactory.getLogger(PracticeExceptionHandler::class.java)

    @ExceptionHandler(PracticeException::class)
    fun handleSearchException(exception: PracticeException): ApiResponse<Any?> {
        val error = exception.error

        val clientMsg = if (error.httpStatusCode.startsWith("4")) {
            error.errorMsg
        } else {
            "일시적인 서버 오류가 발생했습니다"
        }

        return ApiResponse.error(error.errorCode, clientMsg)
    }
}
