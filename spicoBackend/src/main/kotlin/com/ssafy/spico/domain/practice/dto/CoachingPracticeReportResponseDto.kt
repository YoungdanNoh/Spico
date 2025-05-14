package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.entity.SpeedType
import com.ssafy.spico.domain.practice.entity.VolumeType
import com.ssafy.spico.domain.practice.model.CoachingPracticeReport
import java.time.LocalDateTime

data class CoachingPracticeReportResponseDto(
    val projectName: String,
    val practiceName: String,
    val date: LocalDateTime,
    val recordUrl: String,
    val volumeStatus: VolumeType,
    val speedStatus: SpeedType,
    val pauseCount: Int,
    val pronunciationScore: Int
)

fun CoachingPracticeReport.toResponse(): CoachingPracticeReportResponseDto {
    return CoachingPracticeReportResponseDto(
        projectName = this.projectName,
        practiceName = this.practiceName,
        date = this.date,
        recordUrl = this.recordUrl,
        volumeStatus = this.volumeStatus,
        speedStatus = this.speedStatus,
        pauseCount = this.pauseCount,
        pronunciationScore = this.pronunciationScore
    )
}