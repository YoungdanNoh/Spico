package com.a401.spicoandroid.presentation.report.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalReportViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(FinalReportState())
    val state: StateFlow<FinalReportState> = _state.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        viewModelScope.launch {
            _state.value = FinalReportState(
                projectName = "자율 프로젝트",
                roundCount = 5,
                modeType = "파이널 모드",
                score = 84,
                scores = listOf(70f, 85f, 90f, 75f, 95f),
                qnaList = listOf(
                    "Q1. 해당 프로젝트를 하면서 어려웠던 점이 무엇인가요?" to "일정 조율이 힘들었습니다.",
                    "Q2. 프로젝트에 사용한 데이터는 어디에서 구하셨나요?" to "공공데이터를 이용했습니다.",
                    "Q3. 팀원 간 갈등은 어떻게 해결했나요?" to "서로 타협하고 이야기를 했습니다."
                )
            )
        }
    }

    fun deleteReport() {
        // TODO: API 연동 예정
    }
}
