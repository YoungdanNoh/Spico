package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.FinalPracticeInfo

data class StartFinalPracticeResponseDto (
    val practiceId: Int?
)

fun FinalPracticeInfo.toResponse(): StartFinalPracticeResponseDto {
    return StartFinalPracticeResponseDto(practiceId = this.practiceId)
}