package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.CoachingPracticeInfo

data class StartCoachingPracticeResponseDto(
    val practiceId: Int?
)

fun CoachingPracticeInfo.toResponse(): StartCoachingPracticeResponseDto {
    return StartCoachingPracticeResponseDto(practiceId = this.practiceId)
}