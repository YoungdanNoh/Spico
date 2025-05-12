package com.a401.spicoandroid.presentation.finalmode.viewmodel

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.a401.spicoandroid.infrastructure.audio.AudioAnalyzer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class FinalModeViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val audioAnalyzer = AudioAnalyzer()

    private val _waveform = MutableStateFlow<List<Float>>(emptyList())
    val waveform: StateFlow<List<Float>> = _waveform

    var countdown by mutableStateOf(3)
        private set

    var elapsedTime by mutableStateOf("00:00")
        private set

    var isRecording by mutableStateOf(false)
        private set

    var showStopConfirm by mutableStateOf(false)
        private set

    private var recordingStartMillis: Long = 0L
    private var timerJob: Job? = null

    fun startCountdownAndRecording(onStartRecording: () -> Unit) {
        viewModelScope.launch {
            for (i in 3 downTo 1) {
                countdown = i
                delay(1000)
            }
            countdown = 0
            delay(1000)
            countdown = -1 // countdown 완료
            isRecording = true
            onStartRecording()
            startTimer()
        }
    }

    private fun startTimer() {
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

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startAudio() {
        audioAnalyzer.start(viewModelScope) { data ->
            _waveform.value = data
        }
    }

    fun stopAudio() {
        audioAnalyzer.stop()
    }

    fun stopRecording() {
        isRecording = false
        timerJob?.cancel()
    }

    fun showConfirmDialog() {
        showStopConfirm = true
    }

    fun hideConfirmDialog() {
        showStopConfirm = false
    }
}
