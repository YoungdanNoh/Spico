package com.a401.spicoandroid.presentation.coachingmode.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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

    // 대본 상태
    private val _scriptTexts = mutableStateOf<List<String>>(emptyList())

    fun initializeScript(paragraphs: List<String>) {
        _scriptTexts.value = paragraphs
    }

    private fun levenshteinSimilarity(a: String, b: String): Float {
        val distance = levenshteinDistance(a, b)
        val maxLen = maxOf(a.length, b.length).takeIf { it > 0 } ?: 1
        return 1f - (distance.toFloat() / maxLen)
    }

    private fun levenshteinDistance(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j

        for (i in 1..a.length) {
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }

        return dp[a.length][b.length]
    }

    //문단 추적
    fun updateSpokenText(partialText: String) {
        val paragraphList = _scriptTexts.value
        val spoken = partialText.trim().lowercase()

        if (spoken.split(" ").size < 2) return // 너무 짧은 발화는 무시

        var bestParagraphIndex: Int? = null
        var bestScore = 0f

        paragraphList.forEachIndexed { pIndex, paragraph ->
            val sentences = paragraph.split(Regex("[.!?]\\s+"))

            sentences.forEach { sentence ->
                val cleanSentence = sentence.trim().lowercase()
                val startsWith = cleanSentence.startsWith(spoken.take(6))
                val score = levenshteinSimilarity(cleanSentence, spoken)

                if ((startsWith || score > 0.25f) && score > bestScore) {
                    bestScore = score
                    bestParagraphIndex = pIndex
                }
            }
        }

        bestParagraphIndex?.let { index ->
            if (_coachingState.value.currentParagraphIndex != index) {
                Log.d("ScriptTrack", "🔎 문장 기반 추적: 문단=$index, 유사도=$bestScore")
                _coachingState.update { it.copy(currentParagraphIndex = index) }
            }
        }
    }


    // 음성인식
    fun pushWaveformValue(value: Float) {
        _coachingState.update { state ->
            val newList = state.waveform.toMutableList().apply {
                add(value.coerceIn(0.05f, 20f))
                if (size > 50) removeAt(0)
            }
            state.copy(waveform = newList)
        }
    }

    private val _volumeFeedback = MutableStateFlow<String?>(null)
    val volumeFeedback: StateFlow<String?> = _volumeFeedback

    private val _speedFeedback = MutableStateFlow<String?>(null)
    val speedFeedback: StateFlow<String?> = _speedFeedback

    fun updateVolumeFeedback(feedback: String) {
        _volumeFeedback.value = "🎤 $feedback"
    }

    fun updateSpeedFeedback(speed: String) {
        val message = when (speed) {
            "SLOW" -> "🏃 조금 더 빠르게 말해볼까요?"
            "MIDDLE" -> "🏃 지금 속도 좋아요!"
            "FAST" -> "🏃 조금 천천히 말해볼까요!"
            else -> null
        }

        message?.let {
            _speedFeedback.value = it
        }
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
            in 0..69 -> "MIDDLE"
            in 70..89 -> "LOUD"
            else -> "QUIET"
        }
    }

    // 카운트 다운
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

    // 타이머
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

    // 코칭 모드 중지
    fun stopRecording(): Boolean {
        val duration = System.currentTimeMillis() - recordingStartMillis
        if (duration < 30000) return false

        timerJob?.cancel()
        return true
    }

    fun showConfirmDialog() {
        _coachingState.update { it.copy(showStopConfirm = true) }
    }

    fun hideConfirmDialog() {
        _coachingState.update { it.copy(showStopConfirm = false) }
    }


    // 코친모드 결과 전송
    fun postResult(projectId: Int, practiceId: Int) {
        viewModelScope.launch {
            _coachingState.update { it.copy(isLoading = true) }

            val current = _coachingState.value

            val result = postCoachingResultUseCase(
                projectId,
                practiceId,
                CoachingResultRequestDto(
                    fileName = "",
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
