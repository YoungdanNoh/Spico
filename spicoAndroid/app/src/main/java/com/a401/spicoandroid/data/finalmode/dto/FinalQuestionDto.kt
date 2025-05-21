package com.a401.spicoandroid.data.finalmode.dto

import com.a401.spicoandroid.domain.finalmode.model.FinalQuestion

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

fun FinalQuestionDto.toDomain(): FinalQuestion {
    return FinalQuestion(
        id = questionId,
        text = question
    )
}

fun List<FinalQuestionDto>.toDomainList(): List<FinalQuestion> {
    return this.map { it.toDomain() }
}


