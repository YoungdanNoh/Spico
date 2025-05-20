package com.a401.spicoandroid.domain.finalmode.model

import com.a401.spicoandroid.data.finalmode.dto.AnswerDto
import com.a401.spicoandroid.data.finalmode.dto.FinalModeResultRequestDto
import com.a401.spicoandroid.data.finalmode.dto.PauseRecordDto
import com.a401.spicoandroid.data.finalmode.dto.SpeedRecordDto
import com.a401.spicoandroid.data.finalmode.dto.VolumeRecordDto
import com.a401.spicoandroid.infrastructure.speech.model.SpeedType
import com.a401.spicoandroid.infrastructure.speech.model.VolumeLevel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TimeRange(
    val start: String,
    val end: String
)

data class AssessmentResult(
    val transcribedText: String,
    val accuracyScore: Double,
    val completenessScore: Double,
    val fluencyScore: Double,
    val pronunciationScore: Double,
    val pauseScore: Double,
    val volumeScore: Double,
    val speedScore: Double,
    val issueDetails: IssueDetails
) {
    val overallScore: Double
        get() = (accuracyScore + completenessScore + pauseScore + volumeScore + speedScore) / 5
}

data class IssueDetails(
    val pauseIssues: List<TimeRange>,
    val speedIssues: List<TimeRange>,
    val volumeIssues: List<TimeRange>,
    val pronunciationIssues: List<TimeRange>
)

fun AssessmentResult.toFinalModeResultRequestDto(
    answers: List<AnswerDto>
): FinalModeResultRequestDto {

    fun generateFileName(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault()).format(Date())
        return "video/final_result_$timestamp.mp4"
    }

    fun String.toIsoTimeStub(): String {
        val parts = this.split(":")
        val minute = parts.getOrNull(0)?.padStart(2, '0') ?: "00"
        val second = parts.getOrNull(1)?.padStart(2, '0') ?: "00"
        return "2025-05-17T00:$minute:${second}Z"
    }

    fun Double.toVolumeType(): VolumeLevel = when {
        this < 60 -> VolumeLevel.QUIET
        this < 90 -> VolumeLevel.MIDDLE
        else -> VolumeLevel.LOUD
    }

    fun Double.toSpeedType(): SpeedType = when {
        this < 60 -> SpeedType.SLOW
        this < 90 -> SpeedType.MIDDLE
        else -> SpeedType.FAST
    }

    return FinalModeResultRequestDto(
        fileName = generateFileName(),
        speechContent = this.transcribedText,
        pronunciationScore = pronunciationScore.toInt(),
        pauseCount = issueDetails.pauseIssues.size,
        pauseScore = pauseScore.toInt(),
        speedScore = speedScore.toInt(),
        speedStatus = speedScore.toSpeedType(),
        volumeScore = volumeScore.toInt(),
        volumeStatus = volumeScore.toVolumeType(),
        volumeRecords = issueDetails.volumeIssues.map {
            VolumeRecordDto(
                startTime = it.start.toIsoTimeStub(),
                endTime = it.end.toIsoTimeStub(),
                volumeLevel = volumeScore.toVolumeType()
            )
        },
        speedRecords = issueDetails.speedIssues.map {
            SpeedRecordDto(
                startTime = it.start.toIsoTimeStub(),
                endTime = it.end.toIsoTimeStub(),
                speedLevel = speedScore.toSpeedType()
            )
        },
        pauseRecords = issueDetails.pauseIssues.map {
            PauseRecordDto(
                startTime = it.start.toIsoTimeStub(),
                endTime = it.end.toIsoTimeStub(),
            )
        },
        answers = answers
    )
}


