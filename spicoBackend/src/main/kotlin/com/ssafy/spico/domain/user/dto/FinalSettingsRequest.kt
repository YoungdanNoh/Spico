package com.ssafy.spico.domain.user.dto

data class FinalSettingsRequest (
    val hasAudience: Boolean?,
    val hasQnA: Boolean?,
    val questionCount: Int?,
    val answerTimeLimit: Int?
)