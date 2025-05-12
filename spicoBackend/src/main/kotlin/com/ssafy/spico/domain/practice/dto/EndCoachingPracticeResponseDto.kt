package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.PresignedUrl

data class EndCoachingPracticeResponseDto (
    val presignedUrl: String,
)

fun PresignedUrl.toResponse(): EndCoachingPracticeResponseDto {
    return EndCoachingPracticeResponseDto(
        presignedUrl = this.presignedUrl
    )
}