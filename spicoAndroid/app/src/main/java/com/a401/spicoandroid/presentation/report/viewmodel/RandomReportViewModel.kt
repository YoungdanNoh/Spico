package com.a401.spicoandroid.presentation.report.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.presentation.report.dummy.DummyRandomSpeechReport
import com.a401.spicoandroid.domain.report.model.RandomSpeechReport
import kotlinx.coroutines.launch

class RandomReportViewModel : ViewModel() {

    var report by mutableStateOf<RandomSpeechReport?>(null)
        private set

    fun fetchReport(randomSpeechId: Int) {
        // 실제 API 붙이기
        viewModelScope.launch {
            report = DummyRandomSpeechReport
        }
    }
}
