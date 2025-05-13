package com.a401.spicoandroid.data.practice.dto

data class FinalPracticeRequest(
    val hasAudience: Boolean,
    val hasQnA: Boolean,
    val questionCount: Int,
    val answerTimeLimit: Int
)