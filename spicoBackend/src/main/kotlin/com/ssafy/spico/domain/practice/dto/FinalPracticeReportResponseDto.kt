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
    val totalScore: Int,
    val volumeScore: Int,
    val speedScore: Int,
    val pauseScore: Int,
    val pronunciationScore: Int,
    val scriptMatchRate: Int,
    val volumeStatus: VolumeType,
    val speedStatus: SpeedType,
    val pauseCount: Int,
    val volumeRecords: List<VolumeRecord>,
    val speedRecords: List<SpeedRecord>,
    val pauseRecords: List<PauseRecord>,
    val qaRecord: List<QaRecord>

)

fun FinalPracticeReport.tooResponse(): FinalPracticeReportResponseDto {
    return FinalPracticeReportResponseDto(
        projectName = this.projectName,
        practiceName = this.practiceName,
        date = this.date,
        videoUrl = this.videoUrl,
        totalScore = this.totalScore,
        volumeScore = this.volumeScore,
        speedScore = this.speedScore,
        pauseScore = this.pauseScore,
        pronunciationScore = this.pronunciationScore,
        scriptMatchRate = this.scriptMatchRate,
        volumeStatus = this.volumeStatus,
        speedStatus = this.speedStatus,
        pauseCount = this.pauseCount,
        volumeRecords = this.volumeRecords,
        speedRecords = this.speedRecords,
        pauseRecords = this.pauseRecords,
        qaRecord = this.qaRecord
    )
}
