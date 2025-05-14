package com.a401.spicoandroid.presentation.practice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.data.practice.api.PracticeApi
import com.a401.spicoandroid.data.practice.dto.FinalPracticeRequest
import com.a401.spicoandroid.domain.project.model.Project
import com.a401.spicoandroid.domain.project.model.ProjectScreenType
import com.a401.spicoandroid.domain.project.usecase.GetProjectListUseCase
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.practice.usecase.CreateCoachingPracticeUseCase
import com.a401.spicoandroid.domain.practice.usecase.CreateFinalPracticeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val practiceApi: PracticeApi,
    private val getProjectListUseCase: GetProjectListUseCase,
    private val createCoachingPracticeUseCase: CreateCoachingPracticeUseCase,
    private val createFinalPracticeUseCase: CreateFinalPracticeUseCase
) : ViewModel() {

    // 모드: coaching or final
    var selectedMode: PracticeMode? = null

    // 선택된 프로젝트
    var selectedProject: Project? = null

    // 파이널 모드 설정값
    var hasAudience: Boolean = true
    var hasQnA: Boolean = true
    var questionCount: Int = 1
    var answerTimeLimit: Int = 90

    // 생성된 practiceId 저장
    private val _practiceId = MutableStateFlow<Int?>(null)
    val practiceId: StateFlow<Int?> = _practiceId

    // 프로젝트 리스트 상태
    private val _projectList = MutableStateFlow<List<Project>>(emptyList())
    val projectList: StateFlow<List<Project>> = _projectList.asStateFlow()

    // 프로젝트 리스트 가져오기
    fun fetchProjectList() {
        viewModelScope.launch {
            when (val result = getProjectListUseCase(null, 10, ProjectScreenType.HOME)) {
                is DataResource.Success -> {
                    _projectList.value = result.data
                }

                is DataResource.Error -> {
                    Log.e("PracticeViewModel", "프로젝트 목록 불러오기 실패", result.throwable)
                }

                is DataResource.Loading -> {
                    Log.d("PracticeViewModel", "프로젝트 목록 로딩 중 (임시 데이터: ${result.data})")
                }
            }
        }
    }

    fun reset() {
        selectedMode = null
        selectedProject = null
        hasAudience = true
        hasQnA = true
        questionCount = 1
        answerTimeLimit = 90
        _practiceId.value = null
    }

    fun createPractice(
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val projectId = selectedProject?.id ?: return

        viewModelScope.launch {
            when (selectedMode) {
                PracticeMode.COACHING -> {
                    when (val result = createCoachingPracticeUseCase(projectId)) {
                        is DataResource.Success -> {
                            _practiceId.value = result.data
                            onSuccess()
                        }
                        is DataResource.Error -> {
                            onFailure(result.throwable)
                        }
                        is DataResource.Loading -> Unit
                    }
                }

                PracticeMode.FINAL -> {
                    val request = FinalPracticeRequest(
                        hasAudience = hasAudience,
                        hasQnA = hasQnA,
                        questionCount = questionCount,
                        answerTimeLimit = answerTimeLimit
                    )
                    when (val result = createFinalPracticeUseCase(projectId, request)) {
                        is DataResource.Success -> {
                            _practiceId.value = result.data
                            onSuccess()
                        }
                        is DataResource.Error -> {
                            onFailure(result.throwable)
                        }
                        is DataResource.Loading -> Unit
                    }
                }

                null -> onFailure(IllegalStateException("모드가 선택되지 않았습니다."))
            }
        }
    }
}
