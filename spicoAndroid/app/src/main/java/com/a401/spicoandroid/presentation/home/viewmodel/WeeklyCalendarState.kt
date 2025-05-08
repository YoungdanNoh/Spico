package com.a401.spicoandroid.presentation.home.viewmodel

import com.a401.spicoandroid.common.presentation.BaseState
import com.a401.spicoandroid.presentation.home.model.ProjectSchedule
import com.a401.spicoandroid.presentation.home.util.getStartOfWeek
import java.time.LocalDate

data class WeeklyCalendarState(
    val projectList: List<ProjectSchedule> = emptyList(),
    val currentStartDate: LocalDate = getStartOfWeek(LocalDate.now()),

    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    override val toastMessage: String? = null
): BaseState