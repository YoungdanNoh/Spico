package com.a401.spicoandroid.presentation.report.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.a401.spicoandroid.R
import com.a401.spicoandroid.ui.component.TimeSegment

@HiltViewModel
class FinalReportViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(FinalReportState())
    val state: StateFlow<FinalReportState> = _state.asStateFlow()

    val segments = listOf(
        TimeSegment(60000L, 90000L),
        TimeSegment(120000L, 130000L)
    )

    val reportItems = listOf(
        ReportCategoryData(
            title = "발음",
            description = "특정 구간에서 발음이 뭉개져요",
            iconResId = R.drawable.img_feedback_pronunciation,
            timeRangeText = "1:00 ~ 1:30",
            segments = segments
        ),
        ReportCategoryData(
            title = "속도",
            description = "말의 속도가 느린 편이에요",
            iconResId = R.drawable.img_feedback_speed,
            timeRangeText = "1:30 ~ 2:00",
            segments = segments
        ),
        ReportCategoryData(
            title = "휴지",
            description = "휴지 기간이 총 5회 있었어요",
            iconResId = R.drawable.img_feedback_silence,
            timeRangeText = "0:30 ~ 1:00",
            segments = segments
        ),
        ReportCategoryData(
            title = "성량",
            description = "목소리 크기가 많이 작아요",
            iconResId = R.drawable.img_feedback_volume,
            timeRangeText = "0:30 ~ 1:00",
            segments = segments
        ),
        ReportCategoryData(
            title = "대본일치도",
            description = "불일치 문장이 총 5회 있었어요",
            iconResId = R.drawable.img_feedback_script_match,
            timeRangeText = "0:30 ~ 1:00",
            segments = segments
        )
    )


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
                ),
                reportItems = reportItems
            )
        }
    }

    fun deleteReport() {
        // TODO: API 연동 예정
    }
}
