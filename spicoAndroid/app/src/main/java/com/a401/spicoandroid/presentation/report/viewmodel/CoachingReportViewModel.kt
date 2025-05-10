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
class CoachingReportViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CoachingReportState())
    val state: StateFlow<CoachingReportState> = _state.asStateFlow()

    init {
        loadMockReport()
    }

    private fun loadMockReport() {
        viewModelScope.launch {
            _state.value = CoachingReportState(
                projectName = "자율자율 프로젝트",
                roundCount = 5,
                recordUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                volumeStatus = "목소리 크기가 많이 작아요",
                speedStatus = "말의 속도가 느린 편이에요",
                pauseCount = 5,
                pronunciationStatus = "특정 구간에서 발음이 뭉개져요"
            )
        }
    }

    fun deleteReport() {
    }
}