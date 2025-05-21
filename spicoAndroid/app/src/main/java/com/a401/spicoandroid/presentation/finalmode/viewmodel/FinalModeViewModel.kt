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
import com.a401.spicoandroid.domain.finalmode.model.AssessmentResult
import com.a401.spicoandroid.domain.finalmode.model.FinalAnswer
import com.a401.spicoandroid.domain.finalmode.usecase.FinishFinalPracticeUseCase
import com.a401.spicoandroid.domain.finalmode.usecase.GenerateFinalQuestionsUseCase
import com.a401.spicoandroid.domain.practice.usecase.DeletePracticeUseCase
import com.a401.spicoandroid.domain.project.usecase.GetProjectDetailUseCase
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
    private val deletePracticeUseCase: DeletePracticeUseCase,
    private val getProjectDetailUseCase: GetProjectDetailUseCase,

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
        Log.d("AudioDebug", "🎙️ startAudio 호출됨")
        audioAnalyzer.start(viewModelScope) { data ->
            Log.d("AudioDebug", "📈 waveform 데이터 수신: ${data.size}")
            _waveform.value = data
        }
    }


    fun stopAudio() {
        audioAnalyzer.stop()
    }

    // 2. 타이머 관련
    private var answerTimeLimit: Int = 10

    fun setAnswerTimeLimit(value: Int) {
        Log.d("TimerDebug", "🛠️ answerTimeLimit 설정됨: $value")
        answerTimeLimit = value
    }

    var isFirstQuestionStarted by mutableStateOf(false)
        private set

    fun markFirstQuestionStarted() {
        isFirstQuestionStarted = true
    }

    private var perQuestionTimerJob: Job? = null

    private fun startPerQuestionTimer() {
        Log.d("TimerDebug", "⏳ perQuestionTimer 시작: $answerTimeLimit 초 대기")
        perQuestionTimerJob?.cancel()
        perQuestionTimerJob = viewModelScope.launch {
            Log.d("TimerDebug", "⏳ perQuestionTimer 시작: $answerTimeLimit 초 대기")
            delay(answerTimeLimit * 1000L)
            val nextIndex = _currentQuestionIndex.value + 1
            Log.d("TimerDebug", "⏭️ 타이머 완료 → nextIndex=$nextIndex, 질문 개수=${_finalQuestionState.value.questions.size}")

            // 현재 질문의 STT 결과 저장
            val currentQuestion = _finalQuestionState.value.questions.getOrNull(_currentQuestionIndex.value)
            currentQuestion?.let { question ->
                Log.d("FinalFlow", "📝 질문 ${question.id}의 STT 결과 저장 완료")
            }

            if (nextIndex < _finalQuestionState.value.questions.size) {
                _currentQuestionIndex.value = nextIndex
            } else {
                _isAnswerCompleted.value = true
            }
        }
    }


    fun onQuestionStarted() {
        Log.d("TimerDebug", "✅ onQuestionStarted() 호출됨 - 타이머 초기화 시작")
        Log.d("TimerDebug", "💬 현재 answerTimeLimit: $answerTimeLimit")
        elapsedTime = "00:00"
        recordingStartMillis = System.currentTimeMillis()
        startTimer()
        startPerQuestionTimer()
    }

    var countdown by mutableStateOf(3)
        private set

    var elapsedTime by mutableStateOf("00:00")
        private set

    private var recordingStartMillis: Long = 0L
    private var timerJob: Job? = null

    var isRecording by mutableStateOf(false)
        private set

    fun startCountdownAndRecording(onStartRecording: () -> Unit) {
        Log.d("AudioDebug", "⏱️ startCountdownAndRecording 호출됨")
        viewModelScope.launch {
            for (i in 3 downTo 1) {
                countdown = i
                Log.d("AudioDebug", "⏱️ countdown: $i")
                delay(1000)
            }
            countdown = 0
            Log.d("AudioDebug", "🟡 countdown: 0")
            delay(1000)
            countdown = -1
            isRecording = true
            Log.d("AudioDebug", "✅ 카운트다운 종료 → onStartRecording 호출")
            onStartRecording()
            Log.d("AudioDebug", "▶️ onStartRecording 실행됨")
            startTimer()
        }
    }


    fun startTimer() {
        Log.d("TimerDebug", "▶️ startTimer() 시작됨")
        recordingStartMillis = System.currentTimeMillis()
        timerJob = viewModelScope.launch {
            while (isRecording) {
                val elapsedSec = ((System.currentTimeMillis() - recordingStartMillis) / 1000).toInt()
                val minutes = elapsedSec / 60
                val seconds = elapsedSec % 60
                elapsedTime = "%02d:%02d".format(minutes, seconds)
                Log.d("TimerDebug", "🕒 elapsedTime: $elapsedTime")
                delay(1000)
            }
        }
    }

    fun stopRecording() {
        isRecording = false
        timerJob?.cancel()
        perQuestionTimerJob?.cancel()

        // 현재 질문의 STT 결과 저장
        val currentQuestion = _finalQuestionState.value.questions.getOrNull(_currentQuestionIndex.value)
        currentQuestion?.let { question ->
            Log.d("FinalFlow", "📝 질문 ${question.id}의 STT 결과 저장 완료")
        }
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

    private fun startAutoQuestionCycle() {
        questionTimerJob?.cancel() // 이전 타이머 취소
        questionTimerJob = viewModelScope.launch {
            while (true) {
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
        Log.d("FinalFlow", "📤 연습 삭제 요청 전송: projectId=$projectId, practiceId=$practiceId")
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
                else -> {
                    Log.w("FinalFlow", "⚠️ 연습 삭제 응답 없음")
                }
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

    private var answerCount = 0
    private val _isAnswerCompleted = MutableStateFlow(false)
    val isAnswerCompleted: StateFlow<Boolean> get() = _isAnswerCompleted

    fun updateAnswer(questionId: Int, answer: String) {
        val newAnswers = finalQuestionState.value.answers
            .filterNot { it.questionId == questionId } + FinalAnswer(questionId, answer)

        _finalQuestionState.value = finalQuestionState.value.copy(answers = newAnswers)
        answerCount += 1
        if (answerCount == finalQuestionState.value.questions.size) {
            _isAnswerCompleted.value = true
        }
    }


    private val _finalResultState = MutableStateFlow(FinalModeResultState())
    val finalResultState: StateFlow<FinalModeResultState> = _finalResultState.asStateFlow()

    private val _assessmentResult = MutableStateFlow<AssessmentResult?>(null)
    val assessmentResult: StateFlow<AssessmentResult?> = _assessmentResult

    fun setAssessmentResult(result: AssessmentResult) {
        _assessmentResult.value = result
    }

    fun submitFinalModeResult(
        projectId: Int,
        request: FinalModeResultRequestDto
    ) {
        val id = practiceId ?: run {
            Log.e("FinalFlow", "❌ practiceId가 null입니다.")
            return
        }

        Log.d("FinalFlow", "📤 결과 저장 요청: projectId=$projectId, practiceId=$id")
        Log.d("FinalFlow", "📝 저장할 답변 목록: ${finalQuestionState.value.answers}")

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

    // 대본
    private val _scriptState = MutableStateFlow(FinalModeScriptState())
    val scriptState: StateFlow<FinalModeScriptState> = _scriptState.asStateFlow()

    fun setScriptLoading() {
        _scriptState.value = FinalModeScriptState(isLoading = true)
    }

    fun setScript(script: String?) {
        _scriptState.value = FinalModeScriptState(script = script)
        Log.d("FinalFlow", "📝 대본 저장됨 → $script")
    }

    fun setScriptError(e: Throwable) {
        _scriptState.value = FinalModeScriptState(error = e)
        Log.e("FinalFlow", "❌ 대본 로딩 실패", e)
    }

    fun loadProjectScript(projectId: Int) {
        viewModelScope.launch {
            setScriptLoading()
            when (val result = getProjectDetailUseCase(projectId)) {
                is DataResource.Success -> setScript(result.data.script)
                is DataResource.Error -> setScriptError(result.throwable)
                is DataResource.Loading -> {
                    Log.d("FinalFlow", "⏳ 파이널 모드에서 대본 불러오는 중")
                }
            }
        }
    }
}

