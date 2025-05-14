package com.ssafy.spico.domain.user.exception.auth

enum class AuthError (
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
){
    /**
     * JWT
     * 001 ~ 099
     */
    INVALID_TOKEN("401", "A001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("401", "A002", "만료된 토큰입니다."),
    REQUEST_TOKEN_NOT_FOUND("400", "A003", "요청에 토큰이 존재하지 않습니다."),

    /**
     * AUTH
     * 101 ~ 199
     */
    INVALID_KAKAO_TOKEN("401", "A101", "유효하지 않은 카카오 토큰입니다."),
    KAKAO_SERVER_ERROR("502", "A102", "카카오 서버와 통신에 실패했습니다."),
    INVALID_KAKAO_RESPONSE("500", "A103", "카카오 응답을 파싱하는 데 실패했습니다."),
    NOT_AUTHORIZATION_USER("403", "A104", "인가된 사용자가 아닙니다."),
    AUTH_MEMBER_NOT_FOUND("404", "A105", "존재하지 않는 회원입니다."),

    /**
     * COMMON
     * 201 ~ 299
     */
    AUTHENTICATION_ERROR("401", "A201", "Authentication exception"),
    BAD_REQUEST_EXCEPTION("400", "A202", "Bad Request")
}