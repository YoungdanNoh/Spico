package com.a401.spicoandroid.data.finalmode.dto

import com.a401.spicoandroid.domain.finalmode.model.FinalModeResult

data class FinalModeResultRequestDto(
    val fileName: String,
    val speechContent: String,
    val pronunciationScore: Int,
    val pauseCount: Int,
    val pauseScore: Int,
    val speedScore: Int,
    val speedStatus: String,
    val volumeScore: Int,
    val volumeStatus: String,
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
    val volumeLevel: String
)

data class SpeedRecordDto(
    val startTime: String,
    val endTime: String,
    val speedLevel: String
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
