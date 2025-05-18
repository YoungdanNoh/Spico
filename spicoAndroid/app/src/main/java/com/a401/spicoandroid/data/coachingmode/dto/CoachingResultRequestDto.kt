package com.a401.spicoandroid.data.coachingmode.dto

data class CoachingResultRequestDto(
    val fileName: String,
    val pronunciationScore: Int,
    val pauseCount: Int,
    val volumeStatus: String,
    val speedStatus: String
)
