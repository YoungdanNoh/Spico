package com.a401.spicoandroid.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.presentation.home.model.ProjectSchedule
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import com.a401.spicoandroid.presentation.home.util.getStartOfWeek
import com.a401.spicoandroid.presentation.home.util.getWeekDates

class WeeklyCalendarViewModel : ViewModel() {
    private val _projectList = MutableStateFlow<List<ProjectSchedule>>(emptyList())
    val projectList: StateFlow<List<ProjectSchedule>> = _projectList

    private val _currentStartDate = MutableStateFlow(getStartOfWeek(LocalDate.now()))
    val currentWeekDates = _currentStartDate.map { getWeekDates(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, getWeekDates(_currentStartDate.value))

    fun moveToPreviousWeek() {
        _currentStartDate.value = _currentStartDate.value.minusWeeks(1)
    }

    fun moveToNextWeek() {
        _currentStartDate.value = _currentStartDate.value.plusWeeks(1)
    }

    fun updateProjectList(newList: List<ProjectSchedule>) {
        _projectList.value = newList
    }

    fun getMarkedDates(): List<LocalDate> =
        projectList.value.map { LocalDate.parse(it.projectDate.substring(0, 10)) }

    fun findProjectByDate(date: LocalDate): ProjectSchedule? =
        projectList.value.find { LocalDate.parse(it.projectDate.substring(0, 10)) == date }
}

