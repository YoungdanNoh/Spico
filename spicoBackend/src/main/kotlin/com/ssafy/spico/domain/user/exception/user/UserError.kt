package com.ssafy.spico.domain.user.exception.user

enum class UserError (
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
){
    /**
     *
     * 001 ~ 099
     */
    USER_NOT_FOUND("404","U001","존재하지 않는 사용자 ID입니다"),
    USER_INFO_INCOMPLETE("400", "U002", "사용자 정보가 불완전합니다"),


    /**
     * COMMON
     * 201 ~ 299
     */
    BAD_REQUEST_EXCEPTION("400", "U201", "Bad Request")
}