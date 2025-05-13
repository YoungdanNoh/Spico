package com.ssafy.spico.domain.practice.model

import com.ssafy.spico.domain.practice.entity.FeedbackDetail
import java.time.LocalDateTime

data class FeedbackVolumeRecord(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val volumeLevel: FeedbackDetail
)
