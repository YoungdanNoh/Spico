package com.a401.spicoandroid.presentation.report.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.practice.usecase.DeletePracticeUseCase
import com.a401.spicoandroid.domain.report.usecase.GetCoachingReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoachingReportViewModel @Inject constructor(
    private val getCoachingReportUseCase: GetCoachingReportUseCase,
    private val deletePracticeUseCase: DeletePracticeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoachingReportState())
    val state: StateFlow<CoachingReportState> = _state.asStateFlow()

    fun fetchCoachingReport(projectId: Int, practiceId: Int) {
        Log.d("CoachingReport", "📍 fetchCoachingReport() called with projectId=$projectId, practiceId=$practiceId")

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            Log.d("CoachingReport", "🔄 State set to loading")

            when (val result = getCoachingReportUseCase(projectId, practiceId)) {
                is DataResource.Success -> {
                    Log.d("CoachingReport", "✅ API Success: ${result.data}")

                    _state.update {
                        val report = result.data
                        Log.d("CoachingReport", "📝 Report Data: $report")

                        it.copy(
                            projectName = report.projectName,
                            roundCount = report.practiceName.filter { ch -> ch.isDigit() }.toIntOrNull() ?: 0,
                            volumeStatus = when (report.volumeStatus) {
                                "QUIET" -> "아주 적절하게 잘 말했어요!\n성량이 안정적입니다."
                                "MIDDLE" -> "조금 더 톤 조절이 필요해 보여요.\n크거나 작은 구간이 있었어요."
                                "LOUD" -> "전반적으로 괜찮았지만,\n목소리를 조금 더 일정하게 유지해보세요."
                                else -> report.volumeStatus
                            },
                            speedStatus = when (report.speedStatus) {
                                "SLOW" -> "말이 조금 느려요.\n좀 더 빨리 말해볼까요?"
                                "MIDDLE" -> "딱 좋아요!\n적당한 속도였어요."
                                "FAST" -> "말의 속도가 빨라요.\n조금 천천히 말해요."
                                else -> report.speedStatus
                            },
                            pauseCount = report.pauseCount,
                            pronunciationStatus = if (report.pronunciationScore >= 85) {
                                "발음이 정확해요"
                            } else {
                                "특정 구간에서 발음이 부정확해요"
                            },
                            isLoading = false
                        )
                    }
                }

                is DataResource.Error -> {
                    Log.e("CoachingReport", "❌ API Error", result.throwable)
                    _state.update {
                        it.copy(isLoading = false, error = result.throwable)
                    }
                }

                is DataResource.Loading -> {
                    Log.d("CoachingReport", "📡 API Loading 상태 받음")
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
            when (val result = deletePracticeUseCase(projectId, practiceId)) {
                is DataResource.Success -> onSuccess()
                is DataResource.Error -> onError(result.throwable)
                else -> Unit
            }
        }
    }
}