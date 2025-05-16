package com.a401.spicoandroid.common.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * "2025-04-28" → "2025.04.28. 월요일"
 */
fun formatDateWithDay(dateString: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREAN)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. EEEE", Locale.KOREAN)
        LocalDate.parse(dateString, inputFormatter).format(outputFormatter)
    } catch (e: Exception) {
        "--.--.--"
    }
}

/**
 * "2025-04-28" → "2025.04.28"
 */
fun formatDateOnly(dateString: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREAN)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREAN)
        LocalDate.parse(dateString, inputFormatter).format(outputFormatter)
    } catch (e: Exception) {
        "--.--.--"
    }
}

/**
 * "2025-04-28" → "4월 28일"
 */
fun formatMonthDay(date: LocalDate): String {
    return "${date.monthValue}월 ${date.dayOfMonth}일"
}


/**
 * LocalDate → "2025.04.28. 월요일"
 */
fun formatDateWithDay(date: LocalDate): String {
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. EEEE", Locale.KOREAN)
    return date.format(outputFormatter)
}

/**
 * "2025-04-28 15:00" → "2025.04.28. 월요일"
 */
fun formatDateTimeWithDay(dateTimeString: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.KOREAN)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. EEEE", Locale.KOREAN)
        val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        "--.--.--"
    }
}
