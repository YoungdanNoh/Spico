package com.ssafy.spico.domain.practice.model

import com.ssafy.spico.domain.practice.entity.SpeedType
import com.ssafy.spico.domain.practice.entity.VolumeType
import java.time.LocalDateTime

data class FinalPracticeReport(
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
    val feedbackVolumeRecords: List<FeedbackVolumeRecord>,
    val feedbackSpeedRecords: List<FeedbackSpeedRecord>,
    val pauseRecords: List<PauseRecord>,
    val qaRecord: List<QaRecord>
)
