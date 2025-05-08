package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.FinalPracticeSpeechText

data class EndFinalPracticeRequestDto (
    val speechContent: String? = null
)

fun EndFinalPracticeRequestDto.toModel(): FinalPracticeSpeechText {
    return FinalPracticeSpeechText(
        speechContent = this.speechContent
    )
}