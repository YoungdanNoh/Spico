package com.a401.spicoandroid.presentation.randomspeech.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class RandomSpeechReadyViewModel : ViewModel() {

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    fun startCountdown(minutes: Int) {
        val totalSeconds = minutes * 60
        _remainingSeconds.value = totalSeconds
        _isFinished.value = false

        viewModelScope.launch {
            for (sec in totalSeconds downTo 0) {
                _remainingSeconds.value = sec
                delay(1000L)
            }
            _isFinished.value = true
        }
    }

    fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format(Locale.US, "%02d병 %02d초", min, sec)
    }
}
