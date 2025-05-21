package com.a401.spicoandroid.common.utils

import java.time.LocalDate

fun getStartOfWeek(date: LocalDate): LocalDate {
    val dayOfWeek = date.dayOfWeek.value
    return date.minusDays((dayOfWeek - 1).toLong())
}

fun getWeekDates(startDate: LocalDate): List<LocalDate> {
    return (0..6).map { startDate.plusDays(it.toLong()) }
}
