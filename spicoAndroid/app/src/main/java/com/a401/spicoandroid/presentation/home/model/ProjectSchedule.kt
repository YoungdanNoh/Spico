package com.a401.spicoandroid.presentation.home.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ProjectSchedule(
    val projectId: Int,
    val projectName: String,
    val projectDate: String // 예: "2025-04-22 13:00"
) {
    // 날짜 객체로 변환된 값 제공
    val projectDateTime: LocalDateTime
        get() = LocalDateTime.parse(projectDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

    // 달력 마킹용 LocalDate 값 (시간 제거)
    val projectLocalDate: LocalDate
        get() = projectDateTime.toLocalDate()
}
