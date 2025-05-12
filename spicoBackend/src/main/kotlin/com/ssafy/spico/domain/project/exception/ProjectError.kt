package com.ssafy.spico.domain.project.exception

enum class ProjectError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    USER_NOT_FOUND("404", "J001", "사용자를 찾을 수 없습니다."),

    INVALID_PARAMETER("400", "J101", "잘못된 파라미터 값입니다. 올바른 값을 입력해주세요."),
    INVALID_PAGE_SIZE("400", "J102", "페이지의 사이즈를 1 이상으로 지정해주세요."),

    PROJECT_NOT_FOUND("500", "J201", "해당 프로젝트를 찾을 수 없습니다."),
    PERSISTENCE_ERROR("500", "J202", "프로젝트 생성 중 오류가 발생했습니다."),
    INVALID_UPDATE_REQUEST("400", "J203", "프로젝트 수정 중 유효하지 않은 값이 포함되어 있습니다."),
    INVALID_DATE_FORMAT("400", "J204", "날짜 형식이 잘못되었습니다. 올바른 형식을 입력해주세요.")
}