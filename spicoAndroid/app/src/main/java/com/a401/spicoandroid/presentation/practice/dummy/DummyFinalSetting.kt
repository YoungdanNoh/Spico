package com.a401.spicoandroid.presentation.practice.dummy

data class DummyFinalSettingData(
    val hasAudience: Boolean,
    val hasQnA: Boolean,
    val questionCount: Int,
    val answerTimeSec: Int
)

val DummyFinalSettingPreview = DummyFinalSettingData(
    hasAudience = true,
    hasQnA = true,
    questionCount = 3,
    answerTimeSec = 60
)