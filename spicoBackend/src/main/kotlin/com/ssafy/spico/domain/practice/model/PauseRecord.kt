package com.ssafy.spico.domain.practice.model

import java.time.LocalDateTime

data class PauseRecord(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)