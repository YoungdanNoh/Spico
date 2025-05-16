package com.a401.spicoandroid.domain.report.model

data class CoachingReport(
    val projectName: String,
    val practiceName: String,
    val recordUrl: String,
    val volumeStatus: String,
    val speedStatus: String,
    val pauseCount: Int,
    val pronunciationScore: Int
)