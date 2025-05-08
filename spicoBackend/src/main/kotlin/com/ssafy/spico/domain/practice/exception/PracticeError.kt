package com.ssafy.spico.domain.practice.exception

enum class PracticeError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    PROJECT_NOT_FOUND("404", "P001", "프로젝트를 찾을 수 없습니다."),
    PERSISTENCE_ERROR("500", "P002", "연습 정보 저장 중 오류가 발생했습니다."),
    USER_NOT_FOUND("404", "P101", "사용자를 찾을 수 없습니다.")
}
