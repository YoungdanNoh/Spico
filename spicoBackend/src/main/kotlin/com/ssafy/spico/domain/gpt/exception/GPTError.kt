package com.ssafy.spico.domain.practice.exception

enum class GPTError(
    val httpStatusCode: String,
    val errorCode: String,
    val errorMsg: String
) {
    INVALID_INPUT("400", "G001", "STT된 텍스트가 없습니다."),
    GPT_GENERATION_ERROR("500", "G002", "GPT 질문 생성 중 오류가 발생했습니다."),
    GPT_EMPTY_RESPONSE("500", "G003", "GPT 응답이 비어 있습니다."),
    GPT_PARSING_ERROR("500", "G004", "GPT 응답 파싱 중 오류가 발생했습니다.")
}
