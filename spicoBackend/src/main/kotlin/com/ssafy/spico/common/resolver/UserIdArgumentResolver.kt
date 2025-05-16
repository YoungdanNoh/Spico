package com.ssafy.spico.common.resolver

import com.ssafy.spico.common.annotaion.UserId
import com.ssafy.spico.domain.user.exception.user.UserError
import com.ssafy.spico.domain.user.exception.user.UserException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserIdArgumentResolver(

) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(UserId::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest
        return request.getAttribute("userId") ?: throw UserException(UserError.USER_NOT_FOUND)
    }
}