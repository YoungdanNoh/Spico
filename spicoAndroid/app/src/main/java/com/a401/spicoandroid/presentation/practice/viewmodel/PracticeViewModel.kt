package com.a401.spicoandroid.presentation.practice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.practice.dto.FinalPracticeRequest
import com.a401.spicoandroid.domain.practice.usecase.CreateCoachingPracticeUseCase
import com.a401.spicoandroid.domain.practice.usecase.CreateFinalPracticeUseCase
import com.a401.spicoandroid.domain.practice.usecase.GetFinalSettingUseCase
import com.a401.spicoandroid.domain.practice.usecase.SaveFinalSettingUseCase
import com.a401.spicoandroid.domain.project.model.Project
import com.a401.spicoandroid.domain.project.model.ProjectScreenType
import com.a401.spicoandroid.domain.project.usecase.GetProjectListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val getProjectListUseCase: GetProjectListUseCase,
    private val createCoachingPracticeUseCase: CreateCoachingPracticeUseCase,
    private val createFinalPracticeUseCase: CreateFinalPracticeUseCase,
    private val getFinalSettingUseCase: GetFinalSettingUseCase,
    private val saveFinalSettingUseCase: SaveFinalSettingUseCase
) : ViewModel() {

    // 연습 모드: 코칭 or 파이널
    var selectedMode: PracticeMode? = null

    // 선택된 프로젝트 정보
    var selectedProject: Project? = null

    // 파이널 모드 설정 정보 (UI 바인딩용 상태값)
    var hasAudience: Boolean = true
    var hasQnA: Boolean = true
    var questionCount: Int = 1
    var answerTimeLimit: Int = 90 // 단위: 초

    // 생성된 연습 ID
    private val _practiceId = MutableStateFlow<Int?>(null)
    val practiceId: StateFlow<Int?> = _practiceId

    // 프로젝트 리스트 상태
    private val _projectList = MutableStateFlow<List<Project>>(emptyList())
    val projectList: StateFlow<List<Project>> = _projectList.asStateFlow()

    /**
     * 프로젝트 리스트 조회
     * 홈화면 등에서 연습 프로젝트 선택 시 사용됨
     */
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

    /**
     * 파이널 모드 설정 초기화
     * 새 연습 시작 또는 종료 시 상태 초기화용
     */
    fun reset() {
        selectedMode = null
        selectedProject = null
        hasAudience = true
        hasQnA = true
        questionCount = 1
        answerTimeLimit = 90
        _practiceId.value = null
    }

    /**
     * 연습 생성 요청
     * 코칭/파이널 모드에 따라 각각의 API 호출 분기 처리
     */
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

    /**
     * 서버로부터 유저의 파이널 설정값을 불러오기
     * FinalSettingScreen 진입 시 호출됨
     */
    fun fetchFinalSetting() {
        viewModelScope.launch {
            when (val result = getFinalSettingUseCase()) {
                is DataResource.Success -> {
                    val setting = result.data
                    hasAudience = setting.hasAudience
                    hasQnA = setting.hasQnA
                    questionCount = setting.questionCount
                    answerTimeLimit = setting.answerTimeLimit
                }

                is DataResource.Error -> {
                    Log.e("PracticeViewModel", "FinalSetting 불러오기 실패", result.throwable)
                }

                is DataResource.Loading -> Unit
            }
        }
    }

    /**
     * 현재 ViewModel에 저장된 설정값을 서버에 저장 요청
     * 다음 단계(FinalCheckScreen)로 넘어가기 전 호출
     */
    fun saveFinalSetting(
        onSuccess: () -> Unit = {},
        onFailure: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            val request = FinalPracticeRequest(
                hasAudience = hasAudience,
                hasQnA = hasQnA,
                questionCount = questionCount,
                answerTimeLimit = answerTimeLimit
            )
            when (val result = saveFinalSettingUseCase(request)) {
                is DataResource.Success -> {
                    onSuccess()
                }

                is DataResource.Error -> {
                    onFailure(result.throwable)
                }

                is DataResource.Loading -> Unit
            }
        }
    }
}
