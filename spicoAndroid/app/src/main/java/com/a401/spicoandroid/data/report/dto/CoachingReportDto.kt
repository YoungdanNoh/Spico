package com.a401.spicoandroid.data.report.dto

import com.a401.spicoandroid.domain.report.model.CoachingReport

data class CoachingReportDto(
    val projectName: String,
    val practiceName: String,
    val recordUrl: String,
    val volumeStatus: String,
    val speedStatus: String,
    val pauseCount: Int,
    val pronunciationScore: Int
) {
    fun toDomain(): CoachingReport {
        return CoachingReport(
            projectName = projectName,
            practiceName = practiceName,
            recordUrl = recordUrl,
            volumeStatus = volumeStatus,
            speedStatus = speedStatus,
            pauseCount = pauseCount,
            pronunciationScore = pronunciationScore,
        )
    }
}