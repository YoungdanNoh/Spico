package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.FinalPracticeInfo

data class FinalPracticeResponseDto (
    val practiceId: Int?
)

fun FinalPracticeInfo.toResponse(): FinalPracticeResponseDto {
    return FinalPracticeResponseDto(practiceId = this.practiceId)
}