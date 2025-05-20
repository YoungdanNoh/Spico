package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.entity.SpeedType
import com.ssafy.spico.domain.practice.entity.VolumeType
import com.ssafy.spico.domain.practice.model.*
import java.time.LocalDateTime

data class FinalPracticeReportResponseDto(
    val projectName: String,
    val practiceName: String,
    val date: LocalDateTime,
    val videoUrl: String,
    val voiceScript: String,
    val totalScore: Int,
    val volumeScore: Int,
    val speedScore: Int,
    val pauseScore: Int,
    val pronunciationScore: Int,
    val scriptMatchRate: Int,
    val volumeStatus: VolumeType,
    val speedStatus: SpeedType,
    val pauseCount: Int,
    val volumeRecords: List<FeedbackVolumeRecord>,
    val speedRecords: List<FeedbackSpeedRecord>,
    val pauseRecords: List<PauseRecord>,
    val qaRecord: List<QaRecord>
)

fun FinalPracticeReport.toResponse(): FinalPracticeReportResponseDto {
    return FinalPracticeReportResponseDto(
        projectName = this.projectName,
        practiceName = this.practiceName,
        date = this.date,
        videoUrl = this.videoUrl,
        voiceScript = this.voiceScript,
        totalScore = this.totalScore,
        volumeScore = this.volumeScore,
        speedScore = this.speedScore,
        pauseScore = this.pauseScore,
        pronunciationScore = this.pronunciationScore,
        scriptMatchRate = this.scriptMatchRate,
        volumeStatus = this.volumeStatus,
        speedStatus = this.speedStatus,
        pauseCount = this.pauseCount,
        volumeRecords = this.feedbackVolumeRecords,
        speedRecords = this.feedbackSpeedRecords,
        pauseRecords = this.pauseRecords,
        qaRecord = this.qaRecord
    )
}
