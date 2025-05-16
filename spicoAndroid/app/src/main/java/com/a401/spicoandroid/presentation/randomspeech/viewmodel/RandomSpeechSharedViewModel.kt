package com.a401.spicoandroid.presentation.randomspeech.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechInitInfo
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechTopic
import com.a401.spicoandroid.domain.randomspeech.usecase.CreateRandomSpeechUseCase
import com.a401.spicoandroid.domain.randomspeech.usecase.SubmitRandomSpeechScriptUseCase
import com.kakao.sdk.auth.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomSpeechSharedViewModel @Inject constructor(
    private val createRandomSpeechUseCase: CreateRandomSpeechUseCase,
    private val submitRandomSpeechScriptUseCase: SubmitRandomSpeechScriptUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RandomSpeechState())
    val uiState: StateFlow<RandomSpeechState> = _uiState.asStateFlow()

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
        val speechId = _uiState.value.speechId ?: return onError()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = submitRandomSpeechScriptUseCase(speechId, script)) {
                is DataResource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }

                is DataResource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.throwable.message) }
                    onError()
                }

                is DataResource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun reset() {
        _uiState.value = RandomSpeechState()
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
