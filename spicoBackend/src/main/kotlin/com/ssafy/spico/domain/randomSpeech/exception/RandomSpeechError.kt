package com.ssafy.spico.domain.randomSpeech.exception

enum class RandomSpeechError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    USER_NOT_FOUND("404", "S001", "사용자를 찾을 수 없습니다."),

    SPEECH_NOT_FOUND("404", "S101", "랜덤스피치가 존재하지 않습니다."),
    ALREADY_ENDED_SPEECH("404", "S102", "이미 종료된 랜덤스피치입니다.")
}