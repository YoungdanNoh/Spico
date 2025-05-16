package com.ssafy.spico.domain.user.dto

import com.ssafy.spico.domain.user.model.User

data class FinalSettingsResponse (
    val hasAudience: Boolean,
    val hasQnA: Boolean,
    val questionCount: Int,
    val answerTimeLimit: Int
)

fun User.toFinalSettingsResponse(): FinalSettingsResponse {
    return FinalSettingsResponse(
        hasAudience = this.hasAudience,
        hasQnA = this.hasQna,
        questionCount = this.questionCount,
        answerTimeLimit = this.answerTimeLimit
    )
}