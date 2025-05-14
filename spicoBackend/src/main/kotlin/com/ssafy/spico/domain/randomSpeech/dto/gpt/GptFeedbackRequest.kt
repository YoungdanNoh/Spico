package com.ssafy.spico.domain.randomSpeech.dto.gpt

data class GptFeedbackRequest(
    val question: String,
    val newsSummary: String,
    val script: String
)
