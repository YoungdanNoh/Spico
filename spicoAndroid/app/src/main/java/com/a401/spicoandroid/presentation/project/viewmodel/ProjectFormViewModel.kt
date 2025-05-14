package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.usecase.CreateProjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProjectFormViewModel @Inject constructor(
    private val createProjectUseCase: CreateProjectUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectFormState())
    val state: StateFlow<ProjectFormState> = _state.asStateFlow()

    fun resetForm() {
        _state.value = ProjectFormState(
            isLoading = false,
            error = null,
            toastMessage = null
        )
    }

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

    fun createProject(
        onSuccess: (Int) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            val current = _state.value

            _state.update { it.copy(isLoading = true, error = null, toastMessage = null) }

            when (val result = createProjectUseCase(
                name = current.projectName,
                date = current.projectDate?.toString() ?: "",
                time = current.projectTime,
                script = current.script
            )) {
                is DataResource.Success -> {
                    _state.update { it.copy(isLoading = false, toastMessage = "프로젝트가 생성되었습니다.") }
                    onSuccess(result.data)
                }

                is DataResource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.throwable, toastMessage = "에러: ${result.throwable.message}") }
                    onError(result.throwable)
                }

                is DataResource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
