package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProjectFormViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ProjectFormState())
    val state: StateFlow<ProjectFormState> = _state.asStateFlow()

    fun updateProjectName(name: String) {
        _state.value = _state.value.copy(projectName = name)
    }

    fun updateProjectDate(date: LocalDate) {
        _state.value = _state.value.copy(projectDate = date)
    }

    fun updateProjectTime(hour: Int, minute: Int, second: Int) {
        val totalSeconds = hour * 3600 + minute * 60 + second
        _state.value = _state.value.copy(projectTime = totalSeconds)
    }

    fun updateScript(script: String) {
        _state.value = _state.value.copy(script = script)
    }
}
