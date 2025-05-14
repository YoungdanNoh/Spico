package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.model.ProjectScreenType
import com.a401.spicoandroid.domain.project.usecase.DeleteProjectUseCase
import com.a401.spicoandroid.domain.project.usecase.GetProjectListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val getProjectListUseCase: GetProjectListUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase
) : ViewModel() {

    private val _projectListState = MutableStateFlow(ProjectListState())
    val projectListState: StateFlow<ProjectListState> = _projectListState.asStateFlow()

    private val _deleteState = MutableStateFlow(ProjectDeleteState())
    val deleteState: StateFlow<ProjectDeleteState> = _deleteState.asStateFlow()

    fun fetchProjects(
        cursor: Int?,
        size: Int,
        screenType: ProjectScreenType
    ) {
        viewModelScope.launch {
            _projectListState.update { it.copy(isLoading = true, error = null) }

            when (val result = getProjectListUseCase(cursor, size, screenType)) {
                is DataResource.Success -> {
                    _projectListState.update {
                        it.copy(
                            projects = result.data,
                            isLoading = false
                        )
                    }
                }

                is DataResource.Error -> {
                    _projectListState.update {
                        it.copy(
                            isLoading = false,
                            error = result.throwable
                        )
                    }
                }

                is DataResource.Loading -> {
                    _projectListState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun deleteProject(projectId: Int) {
        viewModelScope.launch {
            _deleteState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

            when (val result = deleteProjectUseCase(projectId)) {
                is DataResource.Success -> {
                    _deleteState.update {
                        it.copy(isSuccess = true, isLoading = false)
                    }
                }

                is DataResource.Error -> {
                    _deleteState.update {
                        it.copy(isLoading = false, error = result.throwable)
                    }
                }

                is DataResource.Loading -> {
                    _deleteState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }
    fun resetDeleteState() {
        _deleteState.value = ProjectDeleteState()
    }
}