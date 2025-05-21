package com.ssafy.spico.domain.practice.dto

import com.ssafy.spico.domain.practice.entity.SpeedType
import com.ssafy.spico.domain.practice.entity.VolumeType
import com.ssafy.spico.domain.practice.model.*

data class EndFinalPracticeRequestDto (

    val fileName: String?,
    val speechContent : String?,
    val completenessScore: Int?,
    val pronunciationScore : Int?,
    val pauseCount: Int?,
    val pauseScore: Int?,
    val speedScore: Int?,
    val speedStatus: SpeedType?,
    val volumeScore: Int?,
    val volumeStatus: VolumeType?,
    val volumeRecords: List<FeedbackVolumeRecord>?,
    val speedRecords: List<FeedbackSpeedRecord>?,
    val pauseRecords: List<PauseRecord>?,
    val answers: List<Answer>?
)

fun EndFinalPracticeRequestDto.toModel(): EndFinalPractice {
    return EndFinalPractice(
        fileName = fileName,
        speechContent = this.speechContent,
        completenessScore = this.completenessScore,
        pronunciationScore = this.pronunciationScore,
        pauseCount = this.pauseCount,
        pauseScore = this.pauseScore,
        speedScore = this.speedScore,
        speedStatus = this.speedStatus,
        volumeScore = this.volumeScore,
        volumeStatus = this.volumeStatus,
        volumeRecords = this.volumeRecords,
        speedRecords = this.speedRecords,
        pauseRecords = this.pauseRecords,
        answers = this.answers,
    )
}