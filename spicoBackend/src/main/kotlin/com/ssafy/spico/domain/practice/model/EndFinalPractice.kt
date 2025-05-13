package com.ssafy.spico.domain.practice.model

import com.ssafy.spico.domain.practice.entity.SpeedType
import com.ssafy.spico.domain.practice.entity.VolumeType

data class EndFinalPractice (
    val fileName: String?,
    val speechContent : String?,
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