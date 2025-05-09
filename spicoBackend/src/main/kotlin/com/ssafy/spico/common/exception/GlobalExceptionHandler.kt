package com.ssafy.spico.common.exception

import com.ssafy.spico.common.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception): ApiResponse<Any?> {
        logger.error("Unhandled exception occurred: {}", ex.message, ex)
        return ApiResponse.error("500", "일시적인 서버 오류가 발생했습니다")
    }
}
