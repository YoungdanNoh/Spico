package com.a401.spicoandroid.presentation.report.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.report.model.RandomSpeechReport
import com.a401.spicoandroid.domain.report.usecase.DeleteRandomSpeechUseCase
import com.a401.spicoandroid.domain.report.usecase.GetRandomSpeechReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomReportViewModel @Inject constructor(
    private val getRandomSpeechReportUseCase: GetRandomSpeechReportUseCase,
    private val deleteRandomSpeechUseCase: DeleteRandomSpeechUseCase
) : ViewModel() {

    var report by mutableStateOf<RandomSpeechReport?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var deleteSuccess by mutableStateOf(false)

    fun fetchReport(randomSpeechId: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val result = getRandomSpeechReportUseCase(randomSpeechId)) {
                is DataResource.Success -> {
                    report = result.data
                    isLoading = false
                }

                is DataResource.Error -> {
                    errorMessage = result.throwable.message ?: "리포트 로딩에 실패했어요."
                    isLoading = false
                }

                is DataResource.Loading -> {
                    isLoading = true
                }
            }
        }
    }

    fun deleteReport(id: Int) {
        viewModelScope.launch {
            when (val result = deleteRandomSpeechUseCase(id)) {
                is DataResource.Success -> {
                    deleteSuccess = true
                }

                is DataResource.Error -> {
                    errorMessage = result.throwable.message ?: "삭제에 실패했어요."
                }

                is DataResource.Loading -> {
                    isLoading = true
                }
            }
        }
    }
}
