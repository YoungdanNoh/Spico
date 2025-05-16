package com.a401.spicoandroid.presentation.project.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.model.ProjectScreenType
import com.a401.spicoandroid.domain.project.usecase.DeleteProjectUseCase
import com.a401.spicoandroid.domain.project.usecase.GetProjectListUseCase
import com.a401.spicoandroid.domain.project.usecase.UpdateProjectUseCase
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
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase
) : ViewModel() {

    private val _projectListState = MutableStateFlow(ProjectListState())
    val projectListState: StateFlow<ProjectListState> = _projectListState.asStateFlow()

    private val _deleteState = MutableStateFlow(ProjectDeleteState())
    val deleteState: StateFlow<ProjectDeleteState> = _deleteState.asStateFlow()

    private val _updateState = MutableStateFlow(ProjectUpdateState())
    val updateState: StateFlow<ProjectUpdateState> = _updateState.asStateFlow()

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

    fun updateProject(
        projectId: Int,
        name: String? = null,
        date: String? = null,
        time: Int? = null,
        script: String? = null,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        Log.d("UPDATE", "🚀 updateProject() 진입") // ✅ 1단계 확인용 로그

        viewModelScope.launch {
            _updateState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

            when (val result = updateProjectUseCase(projectId, name, date, time, script)) {
                is DataResource.Success -> {
                    Log.d("UPDATE", "✅ update 성공")
                    _updateState.update { it.copy(isSuccess = true, isLoading = false) }
                    onSuccess()
                }
                is DataResource.Error -> {
                    Log.e("UPDATE", "❌ update 실패: ${result.throwable}", result.throwable)
                    _updateState.update { it.copy(isLoading = false, error = result.throwable) }
                    onError(result.throwable)
                }
                is DataResource.Loading -> {
                    Log.d("UPDATE", "⏳ 로딩 중")
                }
            }
        }
    }


    fun resetUpdateState() {
        _updateState.value = ProjectUpdateState()
    }
}