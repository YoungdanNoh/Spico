package com.ssafy.spico.domain.practice.exception

enum class PracticeError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    USER_NOT_FOUND("404", "P001", "사용자를 찾을 수 없습니다."),

    INVALID_QUESTION_COUNT("400", "P101", "질문 개수는 1~3개여야 합니다."),
    INVALID_ANSWER_TIME_LIMIT("400", "P102", "답변 제한 시간은 30~180초여야 합니다."),

    PRACTICE_NOT_FOUND("400", "P202", "해당 연습을 찾을 수 없습니다."),
    PERSISTENCE_ERROR("500", "P202", "연습 정보 저장 중 오류가 발생했습니다."),

    PROJECT_NOT_FOUND("404", "P301", "프로젝트를 찾을 수 없습니다."),

    REPORT_NOT_FOUND("404", "S001", "리포트를 찾을 수 없습니다."),
    FEEDBACK_NOT_FOUND("404", "S001", "피드백을 찾을 수 없습니다."),

    INVALID_FILENAME("404", "P401","파일의 제목이 없습니다."),
    INVALID_PRONUNCIATION_SCORE("404", "P402", "유효한 발음 점수가 아닙니다."),
    INVALID_PAUSE_COUNT("404", "P403", "유효한 휴지 횟수가 아닙니다."),
    INVALID_SPEED_STATUS("404", "P404", "유효한 발표 속도 평가 값이 아닙니다."),
    INVALID_VOLUME_STATUS("404", "P405", "유효한 성량 평가 값이 아닙니다.")


}
