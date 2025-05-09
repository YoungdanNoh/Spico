package com.ssafy.spico.domain.project.exception

enum class ProjectError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    USER_NOT_FOUND("404", "J001", "사용자를 찾을 수 없습니다."),

    INVALID_PARAMETER("400", "J101", "잘못된 파라미터 값입니다. 올바른 값을 입력해주세요."),
    INVALID_PAGE_SIZE("400", "J102", "페이지의 사이즈를 1 이상으로 지정해주세요."),
}