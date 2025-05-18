package com.a401.spicoandroid.presentation.finalmode.viewmodel

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.data.finalmode.dto.FinalModeResultRequestDto
import com.a401.spicoandroid.domain.finalmode.usecase.FinishFinalPracticeUseCase
import com.a401.spicoandroid.domain.finalmode.usecase.GenerateFinalQuestionsUseCase
import com.a401.spicoandroid.domain.practice.usecase.DeletePracticeUseCase
import com.a401.spicoandroid.infrastructure.audio.AudioAnalyzer
import com.a401.spicoandroid.infrastructure.camera.UploadManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

@HiltViewModel
class FinalModeViewModel @Inject constructor(
    private val generateFinalQuestionsUseCase: GenerateFinalQuestionsUseCase,
    private val finishFinalPracticeUseCase: FinishFinalPracticeUseCase,
    private val uploadManager: UploadManager,
    private val deletePracticeUseCase: DeletePracticeUseCase
) : ViewModel() {

    private var practiceId: Int? = null

    fun setPracticeId(id: Int) {
        practiceId = id
    }


    // 1. 오디오 관련
    private val audioAnalyzer = AudioAnalyzer()

    private val _waveform = MutableStateFlow<List<Float>>(emptyList())
    val waveform: StateFlow<List<Float>> = _waveform

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startAudio() {
        audioAnalyzer.start(viewModelScope) { data ->
            _waveform.value = data
        }
    }

    fun stopAudio() {
        audioAnalyzer.stop()
    }

    // 2. 타이머 관련
    var countdown by mutableStateOf(3)
        private set

    var elapsedTime by mutableStateOf("00:00")
        private set

    private var recordingStartMillis: Long = 0L
    private var timerJob: Job? = null

    var isRecording by mutableStateOf(false)
        private set

    fun startCountdownAndRecording(onStartRecording: () -> Unit) {
        viewModelScope.launch {
            for (i in 3 downTo 1) {
                countdown = i
                delay(1000)
            }
            countdown = 0
            delay(1000)
            countdown = -1
            isRecording = true
            onStartRecording()
            startTimer()
        }
    }

    fun startTimer() {
        recordingStartMillis = System.currentTimeMillis()
        timerJob = viewModelScope.launch {
            while (isRecording) {
                val elapsedSec = ((System.currentTimeMillis() - recordingStartMillis) / 1000).toInt()
                val minutes = elapsedSec / 60
                val seconds = elapsedSec % 60
                elapsedTime = "%02d:%02d".format(minutes, seconds)
                delay(1000)
            }
        }
    }

    fun stopRecording() {
        isRecording = false
        timerJob?.cancel()
    }

    // 3. 다이얼로그
    var showStopConfirm by mutableStateOf(false)
        private set

    var showEarlyExitDialog by mutableStateOf(false)
        private set

    var showNormalExitDialog by mutableStateOf(false)
        private set

    fun showConfirmDialog() {
        showStopConfirm = true
    }

    fun hideConfirmDialog() {
        showStopConfirm = false
    }

    fun checkElapsedAndShowDialog(elapsedSeconds: Int) {
        if (elapsedSeconds < 30) {
            showEarlyExitDialog = true
        } else {
            showNormalExitDialog = true
        }
    }

    fun hideAllDialogs() {
        showEarlyExitDialog = false
        showNormalExitDialog = false
    }

    // 4. 질문 생성 상태 관리
    private val _finalQuestionState = MutableStateFlow(FinalQuestionState())
    val finalQuestionState: StateFlow<FinalQuestionState> = _finalQuestionState.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private var questionTimerJob: Job? = null
    private val questionDisplayDurationMillis = 5000L // 질문 전환 시간(5초)

    private fun startAutoQuestionCycle() {
        questionTimerJob?.cancel() // 이전 타이머 취소
        questionTimerJob = viewModelScope.launch {
            while (true) {
                delay(questionDisplayDurationMillis)
                _finalQuestionState.update { state ->
                    val nextIndex = _currentQuestionIndex.value + 1
                    if (nextIndex < state.questions.size) {
                        _currentQuestionIndex.value = nextIndex
                    } else {
                        cancel() // 마지막 질문이면 타이머 종료
                    }
                    state
                }
            }
        }
    }
    // 연습 삭제
    fun deletePracticeAndExit(
        projectId: Int,
        practiceId: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            when (val result = deletePracticeUseCase(projectId, practiceId)) {
                is DataResource.Success -> {
                    Log.d("FinalFlow", "✅ 연습 삭제 성공")
                    onSuccess()
                }
                is DataResource.Error -> {
                    Log.e("FinalFlow", "❌ 연습 삭제 실패", result.throwable)
                    onError()
                }
                else -> {}
            }
        }
    }


    fun generateFinalQuestions(projectId: Int, practiceId: Int, speechContent: String) {
        viewModelScope.launch {
            _finalQuestionState.update { it.copy(isLoading = true, error = null) }

            when (val result = generateFinalQuestionsUseCase(projectId, practiceId, speechContent)) {
                is DataResource.Success -> {
                    Log.d("FinalFlow", "✅ 질문 생성 성공: ${result.data}")
                    _finalQuestionState.update {
                        it.copy(questions = result.data, isLoading = false)
                    }
                    _currentQuestionIndex.value = 0
                    startAutoQuestionCycle()
                }
                is DataResource.Error -> {
                    Log.e("FinalFlow", "❌ 질문 생성 실패: ${result.throwable}")
                    _finalQuestionState.update {
                        it.copy(error = result.throwable, isLoading = false)
                    }
                }
                is DataResource.Loading -> {
                    _finalQuestionState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private val _finalResultState = MutableStateFlow(FinalModeResultState())
    val finalResultState: StateFlow<FinalModeResultState> = _finalResultState.asStateFlow()

    fun submitFinalModeResult(
        projectId: Int,
        request: FinalModeResultRequestDto
    ) {
        val id = practiceId ?: run {
            Log.e("FinalFlow", "❌ practiceId가 null입니다.")
            return
        }

        Log.d("FinalFlow", "📤 결과 저장 요청: projectId=$projectId, practiceId=$id")
        Log.d("FinalFlow", "📤 결과 저장 요청: projectId=$projectId, practiceId=$id")

        viewModelScope.launch {
            _finalResultState.update { it.copy(isLoading = true, error = null) }

            when (val result = finishFinalPracticeUseCase(projectId, id, request)) {
                is DataResource.Success -> {
                    Log.d("FinalFlow", "✅ 저장 성공: presignedUrl=${result.data.presignedUrl}")
                    _finalResultState.update {
                        it.copy(presignedUrl = result.data.presignedUrl, isLoading = false)
                    }
                }
                is DataResource.Error -> {
                    Log.e("FinalFlow", "❌ 저장 실패: ${result.throwable}")
                    _finalResultState.update {
                        it.copy(error = result.throwable, isLoading = false)
                    }
                }
                is DataResource.Loading -> {
                    Log.d("FinalFlow", "⏳ 저장 중")
                    _finalResultState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun uploadFinalVideo(presignedUrl: String, file: File, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = uploadManager.uploadVideoToPresignedUrl(presignedUrl, file)
            onResult(result)
        }
    }

    private var hasQnA: Boolean = false
    fun setHasQnA(value: Boolean) {
        Log.d("FinalFlow", "📥 setHasQnA called with: $value")
        hasQnA = value
    }

    fun getHasQnA(): Boolean {
        Log.d("FinalFlow", "📤 getHasQnA returns: $hasQnA")
        return hasQnA
    }


}

