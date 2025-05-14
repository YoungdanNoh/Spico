package com.a401.spicoandroid.data.finalmode.dto

data class FinalQuestionDto(
    val questionId: Int,
    val question: String
)

data class FinalQuestionResponseDto(
    val questions: List<FinalQuestionDto>
)

data class SpeechContentRequest(
    val speechContent: String
)
