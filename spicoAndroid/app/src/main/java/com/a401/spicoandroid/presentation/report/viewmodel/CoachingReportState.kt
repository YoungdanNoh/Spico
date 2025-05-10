package com.a401.spicoandroid.presentation.report.viewmodel

data class CoachingReportState(
    val projectName: String = "",
    val roundCount: Int = 0,
    val recordUrl: String = "",
    val volumeStatus: String = "",
    val speedStatus: String = "",
    val pauseCount: Int = 0,
    val pronunciationStatus: String = "",
)