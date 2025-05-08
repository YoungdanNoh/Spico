package com.ssafy.spico.domain.practice.model

data class FinalPracticeSetting (
    val hasAudience: Boolean,
    val hasQnA: Boolean,
    val questionCount: Int,
    val answerTimeLimit: Int
)