package com.a401.spicoandroid.presentation.home.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ProjectSchedule(
    val projectId: Int,
    val projectName: String,
    val projectDate: String // 예: "2025-04-22 13:00"
) {
    val projectDateTime: LocalDateTime
        get() = LocalDateTime.parse(projectDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

    val projectLocalDate: LocalDate
        get() = projectDateTime.toLocalDate()
}
