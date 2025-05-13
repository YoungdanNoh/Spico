package com.ssafy.spico.domain.practice.model

import com.ssafy.spico.domain.practice.entity.FeedbackDetail
import java.time.LocalDateTime

data class FeedbackSpeedRecord(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val speedLevel: FeedbackDetail,
)
