package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.exception.PracticeError
import com.ssafy.spico.domain.practice.exception.PracticeException
import com.ssafy.spico.domain.practice.model.FinalPracticeSetting

data class StartFinalPracticeRequestDto (
    val hasAudience: Boolean,
    val hasQnA: Boolean,
    val questionCount: Int,
    val answerTimeLimit: Int
)

fun StartFinalPracticeRequestDto.toModel(): FinalPracticeSetting {
    if (questionCount !in 1..3) {
        throw PracticeException(PracticeError.INVALID_QUESTION_COUNT)
    }
    if (answerTimeLimit !in 30..180) {
        throw PracticeException(PracticeError.INVALID_ANSWER_TIME_LIMIT)
    }

    return FinalPracticeSetting(
        hasAudience = hasAudience,
        hasQnA = hasQnA,
        questionCount = questionCount,
        answerTimeLimit = answerTimeLimit
    )
}