package com.a401.spicoandroid.presentation.report.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.practice.usecase.DeletePracticeUseCase
import com.a401.spicoandroid.domain.report.model.*
import com.a401.spicoandroid.domain.report.usecase.GetFinalReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalReportViewModel @Inject constructor(
    private val getFinalReportUseCase: GetFinalReportUseCase,
    private val deletePracticeUseCase: DeletePracticeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FinalReportState())
    val state: StateFlow<FinalReportState> = _state.asStateFlow()

    fun fetchFinalReport(projectId: Int, practiceId: Int) {
        viewModelScope.launch {
            Log.d("FinalReport", "📡 API 호출 시작: projectId=$projectId, practiceId=$practiceId")
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = getFinalReportUseCase(projectId, practiceId)) {
                is DataResource.Success -> {
                    Log.d("FinalReport", "✅ 응답 성공: ${result.data}")
                    _state.value = result.data.toUiState().copy(isLoading = false)
                }

                is DataResource.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.throwable)
                    }
                }

                is DataResource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun deleteReport(
        projectId: Int,
        practiceId: Int,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            Log.d("PracticeList", "🧨 deleteReport 호출됨: projectId=$projectId, practiceId=$practiceId")

            when (val result = deletePracticeUseCase(projectId, practiceId)) {
                is DataResource.Success -> {
                    Log.d("PracticeList", "✅ deleteReport 성공")
                    onSuccess()
                }
                is DataResource.Error -> {
                    Log.e("PracticeList", "❌ deleteReport 실패: ${result.throwable}", result.throwable)
                    onError(result.throwable)
                }
                else -> Unit
            }
        }
    }

}
