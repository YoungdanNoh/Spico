package com.a401.spicoandroid.presentation.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.model.ProjectDetail
import com.a401.spicoandroid.domain.project.usecase.GetProjectDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val getProjectDetailUseCase: GetProjectDetailUseCase
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

    suspend fun fetchProjectDetailAndGet(projectId: Int): ProjectDetail {
        // 상세 요청 실행
        fetchProjectDetail(projectId)

        // 최대 1초 동안 100ms 간격으로 polling (state 값이 채워질 때까지 기다림)
        repeat(10) {
            val current = state.value.project
            if (current != null) return current
            delay(100) // 0.1초 기다리기
        }

        throw IllegalStateException("❌ 프로젝트 상세 정보가 1초 내에 로드되지 않았습니다.")
    }
}
