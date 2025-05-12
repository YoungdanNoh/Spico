package com.ssafy.spico.domain.practice.model

import com.ssafy.spico.domain.practice.entity.SpeedType
import com.ssafy.spico.domain.practice.entity.VolumeType

data class EndCoachingPractice (
    val fileName: String?,
    val pronunciationScore: Int?,
    val pauseCount: Int?,
    val volumeStatus: VolumeType?,
    val speedStatus: SpeedType?
)