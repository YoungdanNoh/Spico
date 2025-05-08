package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.FinalPracticeQuestionList

data class EndFinalPracticeResponseDto(
    val questions: List<String>
)

fun FinalPracticeQuestionList.toResponse(): EndFinalPracticeResponseDto {
    return EndFinalPracticeResponseDto(questions = this.questions)
}