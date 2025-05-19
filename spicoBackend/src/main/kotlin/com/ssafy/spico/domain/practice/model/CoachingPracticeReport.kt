package com.ssafy.spico.domain.practice.model

import com.ssafy.spico.domain.practice.entity.SpeedType
import com.ssafy.spico.domain.practice.entity.VolumeType
import java.time.LocalDateTime

data class CoachingPracticeReport(
    val projectName: String,
    val practiceName: String,
    val date: LocalDateTime,
    val volumeStatus: VolumeType,
    val speedStatus: SpeedType,
    val pauseCount: Int,
    val pronunciationScore: Int
)
