package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.model.FinalPracticeSetting

data class FinalPracticeRequestDto (
    val hasAudience: Boolean,
    val hasQnA: Boolean,
    val questionCount: Int,
    val answerTimeLimit: Int
)

fun FinalPracticeRequestDto.toModel(): FinalPracticeSetting {
    require(questionCount in 1..3) { "질문 개수는 1~3개여야 합니다." }
    require(answerTimeLimit in 1..60) { "답변 제한 시간은 1~60초여야 합니다." }

    return FinalPracticeSetting(
        hasAudience = hasAudience,
        hasQnA = hasQnA,
        questionCount = questionCount,
        answerTimeLimit = answerTimeLimit
    )
}