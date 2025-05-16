package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.usecase.GetProjectDetailUseCase
import com.a401.spicoandroid.domain.project.usecase.UpdateProjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val getProjectDetailUseCase: GetProjectDetailUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectDetailState())
    val state: StateFlow<ProjectDetailState> = _state.asStateFlow()

    fun fetchProjectDetail(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = getProjectDetailUseCase(projectId)) {
                is DataResource.Success -> {
                    _state.update { it.copy(id = projectId, project = result.data, isLoading = false) }
                }
                is DataResource.Error -> {
                    _state.update { it.copy(error = result.throwable, isLoading = false) }
                }
                is DataResource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
