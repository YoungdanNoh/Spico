package com.ssafy.spico.domain.randomSpeech.exception

enum class RandomSpeechError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    USER_NOT_FOUND("404", "S001", "사용자를 찾을 수 없습니다."),

    SPEECH_NOT_FOUND("404", "S101", "랜덤스피치가 존재하지 않습니다."),
    ALREADY_ENDED_SPEECH("404", "S102", "이미 종료된 랜덤스피치입니다."),
    UNAUTHORIZED_ACCESS("403", "S103", "해당 랜덤스피치에 접근할 권한이 없습니다."),

    REPORT_NOT_FOUND("404", "S201", "리포트를 찾을 수 없습니다."),
    CONTENT_NOT_FOUND("404", "S202", "질문/뉴스를 찾을 수 없습니다."),

    GPT_QUESTION_FAILED("500", "S301", "GPT 질문 생성에 실패했습니다."),
    GPT_FEEDBACK_FAILED("500", "S301", "GPT 피드백 생성에 실패했습니다."),

    NEWS_API_FAILED("502", "S401", "뉴스 API 호출에 실패했습니다."),
}