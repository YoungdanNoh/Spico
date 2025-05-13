package com.a401.spicoandroid.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import com.a401.spicoandroid.common.utils.getWeekDates
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeeklyCalendarViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(WeeklyCalendarState())
    val state: StateFlow<WeeklyCalendarState> = _state.asStateFlow()

    val currentWeekDates: StateFlow<List<LocalDate>> = state
        .map { getWeekDates(it.currentStartDate) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, getWeekDates(_state.value.currentStartDate))

    val markedDates: StateFlow<List<LocalDate>> = state
        .map { state ->
            state.projectList.map { LocalDate.parse(it.projectDate.substring(0, 10)) }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun moveToPreviousWeek() {
        _state.update {
            it.copy(currentStartDate = it.currentStartDate.minusWeeks(1))
        }
    }

    fun moveToNextWeek() {
        _state.update {
            it.copy(currentStartDate = it.currentStartDate.plusWeeks(1))
        }
    }

    fun updateProjectList(newList: List<ProjectSchedule>) {
        _state.update {
            it.copy(projectList = newList)
        }
    }

    fun findProjectByDate(date: LocalDate): ProjectSchedule? =
        state.value.projectList.find { LocalDate.parse(it.projectDate.substring(0, 10)) == date }
}