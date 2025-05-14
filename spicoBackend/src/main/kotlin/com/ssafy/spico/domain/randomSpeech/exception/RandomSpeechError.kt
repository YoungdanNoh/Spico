package com.ssafy.spico.domain.randomSpeech.exception

enum class RandomSpeechError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    USER_NOT_FOUND("404", "J001", "사용자를 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS("403", "J002", "해당 프로젝트에 대한 접근 권한이 없습니다."),
}