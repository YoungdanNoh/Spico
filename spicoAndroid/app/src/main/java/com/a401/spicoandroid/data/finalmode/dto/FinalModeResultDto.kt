package com.a401.spicoandroid.data.finalmode.dto

import com.a401.spicoandroid.domain.finalmode.model.FinalModeResult
import com.a401.spicoandroid.infrastructure.speech.model.SpeedType
import com.a401.spicoandroid.infrastructure.speech.model.VolumeLevel

data class FinalModeResultRequestDto(
    val fileName: String,
    val speechContent: String,
    val completenessScore: Int,
    val pronunciationScore: Int,
    val pauseCount: Int,
    val pauseScore: Int,
    val speedScore: Int,
    val speedStatus: SpeedType,
    val volumeScore: Int,
    val volumeStatus: VolumeLevel,
    val volumeRecords: List<VolumeRecordDto>,
    val speedRecords: List<SpeedRecordDto>,
    val pauseRecords: List<PauseRecordDto>,
    val answers: List<AnswerDto>
)

data class FinalModeResultResponseDto(
    val presignedUrl: String
)

data class VolumeRecordDto(
    val startTime: String,
    val endTime: String,
    val volumeLevel: VolumeLevel
)

data class SpeedRecordDto(
    val startTime: String,
    val endTime: String,
    val speedLevel: SpeedType
)

data class PauseRecordDto(
    val startTime: String,
    val endTime: String
)

data class AnswerDto(
    val questionId: Int,
    val answer: String
)

fun FinalModeResultResponseDto.toDomain(): FinalModeResult {
    return FinalModeResult(presignedUrl = this.presignedUrl ?: "")
}
