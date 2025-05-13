package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.FinalPracticeSpeechText

data class GenerateGPTQuestionRequestDto (
    val speechContent: String? = null
)

fun GenerateGPTQuestionRequestDto.toModel(): FinalPracticeSpeechText {
    return FinalPracticeSpeechText(
        speechContent = this.speechContent
    )
}