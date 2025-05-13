package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.FinalPracticeQuestionList
import com.ssafy.spico.domain.practice.model.Question

data class GenerateGPTQuestionResponseDto(
    val questions: List<Question>
)

fun FinalPracticeQuestionList.toResponse(): GenerateGPTQuestionResponseDto {
    return GenerateGPTQuestionResponseDto(
        questions = this.questions
    )
}