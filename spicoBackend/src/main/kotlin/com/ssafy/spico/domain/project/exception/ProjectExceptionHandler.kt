package com.ssafy.spico.domain.project.exception

import com.ssafy.spico.common.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
@Order(2)
class ProjectExceptionHandler {

    private val logger = LoggerFactory.getLogger(ProjectExceptionHandler::class.java)

    // 비즈니스 로직(Service)에서 발생하는 에러 처리
    @ExceptionHandler(ProjectException::class)
    fun handleProjectException(exception: ProjectException): ApiResponse<Any?> {
        val error = exception.error
        
        val clientMsg = if (error.httpStatusCode.startsWith("4")) {
            error.errorMsg
        } else {
            "일시적인 서버 오류가 발생했습니다"
        }

        return ApiResponse.error(error.errorCode, clientMsg)
    }

    // 클라이언트 요청(Controller)에서 발생하는 에러 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(exception: MethodArgumentTypeMismatchException): ApiResponse<Any?> {
        val error = ProjectError.INVALID_PARAMETER

        logger.error("잘못된 파라미터: ${exception.parameter.parameterName} = ${exception.value} (타입: ${exception.parameter.parameterType})")

        return ApiResponse.error(error.errorCode, error.errorMsg)
    }
}