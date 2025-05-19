package com.a401.spicoandroid.presentation.randomspeech.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechInitInfo
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechTopic
import com.a401.spicoandroid.domain.randomspeech.usecase.CreateRandomSpeechUseCase
import com.a401.spicoandroid.domain.randomspeech.usecase.SubmitRandomSpeechScriptUseCase
import com.a401.spicoandroid.domain.report.usecase.DeleteRandomSpeechUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomSpeechSharedViewModel @Inject constructor(
    private val createRandomSpeechUseCase: CreateRandomSpeechUseCase,
    private val submitRandomSpeechScriptUseCase: SubmitRandomSpeechScriptUseCase,
    private val deleteRandomSpeechUseCase: DeleteRandomSpeechUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RandomSpeechState())
    val uiState: StateFlow<RandomSpeechState> = _uiState.asStateFlow()

    // 종료 후, 로직
    var shouldRedirectToReport by mutableStateOf(true)
        private set

    fun disableAutoRedirect() {
        shouldRedirectToReport = false
    }


    fun setTopic(topic: RandomSpeechTopic) {
        _uiState.update { it.copy(topic = topic) }
    }

    fun setTime(prepTimeSec: Int, speakTimeSec: Int) {
        _uiState.update { it.copy(prepTime = prepTimeSec, speakTime = speakTimeSec) }
    }

    fun createSpeech(onSuccess: () -> Unit, onError: (String?) -> Unit) {
        val topic = _uiState.value.topic ?: return handleError("주제가 설정되지 않았습니다.", onError)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = createRandomSpeechUseCase(
                topic.name,
                _uiState.value.prepTime,
                _uiState.value.speakTime
            )) {
                is DataResource.Success -> {
                    updateStateWithSpeechInfo(result.data)
                    onSuccess()
                }

                is DataResource.Error -> {
                    handleError(result.throwable.message ?: "알 수 없는 오류가 발생했습니다.", onError)
                }

                is DataResource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun submitScript(script: String, onSuccess: () -> Unit, onError: () -> Unit) {
        val speechId = _uiState.value.speechId
        if (speechId == null) {
            Log.e("SubmitScript", "❌ speechId is null")
            onError()
            return
        }

        // 🧼 Whisper 결과 정제 및 fallback 메시지 처리
        val cleanedScript = script.trim()
        val finalScript = if (cleanedScript.length < 5 || cleanedScript.contains("zeoranger", ignoreCase = true)) {
            "음성 인식이 잘되지 않았어요. 다시 시도해 주세요."
        } else {
            cleanedScript
        }

        Log.d("SubmitScript", "🚀 speechId: $speechId")
        Log.d("SubmitScript", "📝 script: $finalScript")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = submitRandomSpeechScriptUseCase(speechId, finalScript)) {
                is DataResource.Success -> {
                    Log.d("SubmitScript", "✅ 성공: 리포트 저장 완료")
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }

                is DataResource.Error -> {
                    Log.e("SubmitScript", "❌ 실패: ${result.throwable.message}", result.throwable)
                    Log.e("SubmitScript", "📛 throwable 클래스: ${result.throwable::class.java.simpleName}")
                    Log.e("SubmitScript", "📛 throwable 전체 내용: ${result.throwable}")
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.throwable.message) }
                    onError()
                }

                is DataResource.Loading -> {
                    Log.d("SubmitScript", "⏳ 로딩 중")
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun deleteSpeech(onSuccess: () -> Unit, onError: () -> Unit) {
        val speechId = _uiState.value.speechId
        if (speechId == null) {
            Log.e("DeleteSpeech", "❌ speechId is null")
            onError()
            return
        }

        Log.d("DeleteSpeech", "🗑️ delete speechId = $speechId")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = deleteRandomSpeechUseCase(speechId)) {
                is DataResource.Success -> {
                    Log.d("DeleteSpeech", "✅ 삭제 성공")
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }

                is DataResource.Error -> {
                    Log.e("DeleteSpeech", "❌ 삭제 실패: ${result.throwable.message}")
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.throwable.message) }
                    onError()
                }

                is DataResource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }


    fun reset() {
        _uiState.value = RandomSpeechState()
        shouldRedirectToReport = true
    }

    fun getSpeechIdForReport(): Int? = uiState.value.speechId

    private fun updateStateWithSpeechInfo(info: RandomSpeechInitInfo) {
        _uiState.update {
            it.copy(
                speechId = info.id,
                question = info.question,
                newsTitle = info.newsTitle,
                newsUrl = info.newsUrl,
                newsSummary = info.newsSummary,
                isLoading = false
            )
        }
    }

    private fun handleError(message: String?, onError: (String?) -> Unit) {
        _uiState.update { it.copy(isLoading = false, errorMessage = message) }
        onError(message)
    }
}
