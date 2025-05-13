package com.a401.spicoandroid.common.utils


import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * "2025-04-28 15:00" → "2025.04.28. 월요일"
 */
fun formatDateWithDay(dateTimeString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.KOREAN)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. EEEE", Locale.KOREAN)
    return LocalDateTime.parse(dateTimeString, inputFormatter).format(outputFormatter)
}

/**
 * "2025-04-28 15:00" → "2025.04.28"
 */
fun formatDateOnly(dateTimeString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.KOREAN)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREAN)
    return LocalDateTime.parse(dateTimeString, inputFormatter).format(outputFormatter)
}

/**
 * "2025-04-28 15:00" → "15:00"
 */
fun formatTimeOnly(dateTimeString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.KOREAN)
    val outputFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.KOREAN)
    return LocalDateTime.parse(dateTimeString, inputFormatter).format(outputFormatter)
}

fun formatDateWithDay(date: LocalDate): String {
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. EEEE", Locale.KOREAN)
    return date.format(outputFormatter)
}
