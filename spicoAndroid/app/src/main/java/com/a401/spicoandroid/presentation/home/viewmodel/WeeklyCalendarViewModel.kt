package com.a401.spicoandroid.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.getWeekDates
import com.a401.spicoandroid.domain.home.model.ProjectSchedule
import com.a401.spicoandroid.domain.project.model.ProjectScreenType
import com.a401.spicoandroid.domain.project.usecase.GetProjectListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeeklyCalendarViewModel @Inject constructor(
    private val getProjectListUseCase: GetProjectListUseCase
) : ViewModel() {

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
        state.value.projectList.find {
            LocalDate.parse(it.projectDate.substring(0, 10)) == date
        }

    fun fetchCalendarProjects() {
        viewModelScope.launch {
            when (val result = getProjectListUseCase(
                cursor = null,
                size = 100,
                screenType = ProjectScreenType.CAL
            )) {
                is DataResource.Success -> {
                    val projectSchedules = result.data.map { project ->
                        ProjectSchedule(
                            projectId = project.id,
                            projectName = project.title,
                            projectDate = project.date.toString() // "2025-04-28"
                        )
                    }
                    updateProjectList(projectSchedules)
                }

                is DataResource.Error -> {
                    // TODO: 에러 처리 필요 시 작성
                }

                else -> Unit
            }
        }
    }
}
