package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.PresignedUrl

data class EndFinalPracticeResponseDto (
    val presignedUrl: String,
)

fun PresignedUrl.toEndFinalResponse(): EndFinalPracticeResponseDto {
    return EndFinalPracticeResponseDto(
        presignedUrl = this.presignedUrl
    )
}