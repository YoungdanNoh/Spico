package com.a401.spicoandroid.presentation.practice.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class PracticeFlowState(
    val mode: PracticeMode? = null,
    val selectedProjectId: Int? = null,
    val currentStep: PracticeStep = PracticeStep.MODE_SELECT,
    val finalSetting: FinalSettingState = FinalSettingState(),
)

enum class PracticeMode { COACHING, FINAL }

enum class PracticeStep {
    MODE_SELECT,
    PROJECT_SELECT,
    FINAL_SETTING,
    FINAL_SCREEN_CHECK,
}

data class FinalSettingState(
    val questionCount: Int = 1,
    val answerDurationSec: Int = 90,
    val isAudienceEnabled: Boolean = true,
    val isQnaEnabled: Boolean = true,
)

@HiltViewModel
class PracticeFlowViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PracticeFlowState())
    val uiState: StateFlow<PracticeFlowState> = _uiState

    // ▶ 모드 선택
    fun selectMode(mode: PracticeMode) {
        _uiState.value = PracticeFlowState(
            mode = mode,
            currentStep = PracticeStep.PROJECT_SELECT
        )
    }

    // ▶ 프로젝트 선택
    fun selectProject(projectId: Int) {
        _uiState.value = _uiState.value.copy(
            selectedProjectId = projectId,
            currentStep = when (_uiState.value.mode) {
                PracticeMode.FINAL -> PracticeStep.FINAL_SETTING
                PracticeMode.COACHING -> PracticeStep.MODE_SELECT // 코칭은 바로 실행
                null -> PracticeStep.MODE_SELECT
            }
        )
    }

    // ▶ 설정 저장 (예: 파이널 설정)
    fun updateFinalSetting(setting: FinalSettingState) {
        _uiState.value = _uiState.value.copy(
            finalSetting = setting,
            currentStep = PracticeStep.FINAL_SCREEN_CHECK
        )
    }

    // 돌아가기: 설정 → 프로젝트
    fun backToProjectSelect() {
        _uiState.value = _uiState.value.copy(
            currentStep = PracticeStep.PROJECT_SELECT,
            finalSetting = FinalSettingState() // 설정 초기화
        )
    }

    // 돌아가기: 프로젝트 → 모드 선택
    fun backToModeSelect() {
        _uiState.value = PracticeFlowState() // 전체 초기화
    }

    // (필요시) 흐름 완전 초기화
    fun resetFlow() {
        _uiState.value = PracticeFlowState()
    }
}