package com.a401.spicoandroid.presentation.coachingmode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.coachingmode.dto.CoachingResultRequestDto
import com.a401.spicoandroid.domain.coachingmode.usecase.PostCoachingResultUseCase
import com.a401.spicoandroid.infrastructure.speech.model.VolumeRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoachingModeViewModel @Inject constructor(
    private val postCoachingResultUseCase: PostCoachingResultUseCase,
) : ViewModel() {

    private val _coachingState = MutableStateFlow(CoachingModeState(isSuccess = false))
    val coachingState: StateFlow<CoachingModeState> = _coachingState

    private var timerJob: Job? = null
    private var recordingStartMillis: Long = 0L

    fun pushWaveformValue(value: Float) {
        _coachingState.update { state ->
            val newList = state.waveform.toMutableList().apply {
                add(value.coerceIn(0.05f, 20f))
                if (size > 50) removeAt(0)
            }
            state.copy(waveform = newList)
        }
    }

    fun updateVolumeFeedback(feedback: String) {
        _coachingState.update { it.copy(volumeFeedback = feedback) }
    }

    fun updatePauseCount(count: Int) {
        _coachingState.update { it.copy(pauseCount = count) }
    }

    fun updateSpeedStatus(status: String) {
        _coachingState.update { it.copy(speedStatus = status) }
    }

    fun calculateAndStoreVolumeScore(records: List<VolumeRecord>) {
        val filtered = records.drop(1)
        if (filtered.isEmpty()) {
            _coachingState.update { it.copy(volumeScore = 100) }
            return
        }

        val total = filtered.size.toFloat()
        val loud = filtered.count { it.volumeLevel.name == "LOUD" }
        val quiet = filtered.count { it.volumeLevel.name == "QUIET" }
        val middle = filtered.count { it.volumeLevel.name == "MIDDLE" }

        val loudRatio = loud / total
        val quietRatio = quiet / total
        val middleRatio = middle / total

        var score = 100
        score += ((middleRatio - 0.5f) * 40).toInt()
        score -= (loudRatio * 30).toInt()
        score -= (quietRatio * 20).toInt()

        _coachingState.update { it.copy(volumeScore = score.coerceIn(0, 100)) }
    }

    private fun volumeScoreToStatus(score: Int): String {
        return when (score) {
            in 0..39 -> "QUIET"
            in 40..69 -> "MIDDLE"
            else -> "LOUD"
        }
    }

    fun startCountdownAndRecording(onStartSTT: () -> Unit) {
        viewModelScope.launch {
            Log.d("Countdown", "⏳ 카운트다운 시작")

            for (i in 3 downTo 1) {
                _coachingState.update { it.copy(countdown = i) }
                Log.d("Countdown", "현재: $i")
                delay(1000)
            }

            _coachingState.update { it.copy(countdown = 0) }
            Log.d("Countdown", "현재: 0")
            delay(1000)

            _coachingState.update { it.copy(countdown = -1) }
            Log.d("Countdown", "카운트다운 종료")

            recordingStartMillis = System.currentTimeMillis()
            startTimer()
            onStartSTT()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val totalElapsed = now - recordingStartMillis
                val seconds = (totalElapsed / 1000).toInt()
                val minutes = seconds / 60
                val sec = seconds % 60
                val formatted = "%02d:%02d".format(minutes, sec)

                _coachingState.update { it.copy(elapsedTime = formatted) }
                delay(1000)
            }
        }
    }

    fun stopRecording(): Boolean {
        val duration = System.currentTimeMillis() - recordingStartMillis
        if (duration < 1000) return false

        timerJob?.cancel()
        return true
    }

    fun showConfirmDialog() {
        _coachingState.update { it.copy(showStopConfirm = true) }
    }

    fun hideConfirmDialog() {
        _coachingState.update { it.copy(showStopConfirm = false) }
    }

    fun postResult(projectId: Int, practiceId: Int) {
        viewModelScope.launch {
            _coachingState.update { it.copy(isLoading = true) }

            val current = _coachingState.value

            val result = postCoachingResultUseCase(
                projectId,
                practiceId,
                CoachingResultRequestDto(
                    fileName = "audio1.mp3",
                    pronunciationScore = 0,
                    pauseCount = current.pauseCount,
                    volumeStatus = volumeScoreToStatus(current.volumeScore),
                    speedStatus = current.speedStatus
                )
            )

            when (result) {
                is DataResource.Success -> {
                    Log.d("PostResult", "✅ API 성공, 리포트로 이동 준비")
                    _coachingState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                }

                is DataResource.Error -> {
                    Log.d("PostResult", "✅ API 실패")
                    _coachingState.update {
                        it.copy(
                            isLoading = false,
                            error = result.throwable
                        )
                    }
                }

                is DataResource.Loading -> {
                    _coachingState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }
}
