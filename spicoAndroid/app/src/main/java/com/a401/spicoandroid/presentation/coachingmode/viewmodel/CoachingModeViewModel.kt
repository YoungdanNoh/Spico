package com.a401.spicoandroid.presentation.coachingmode.viewmodel

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.infrastructure.audio.AudioRecorderService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CoachingModeViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val audioRecorder = AudioRecorderService(application)

    private val _waveform = MutableStateFlow<List<Float>>(emptyList())
    val waveform: StateFlow<List<Float>> = _waveform

    var countdown by mutableIntStateOf(3)
        private set

    var elapsedTime by mutableStateOf("00:00")
        private set

    var showStopConfirm by mutableStateOf(false)
        private set

    var recordingState by mutableStateOf(RecordingState.STOPPED)
        private set

    var recordedFile: File? by mutableStateOf(null)
        private set

    var onRecordingComplete: ((File) -> Unit)? = null

    private var timerJob: Job? = null
    private var elapsedAccumulatedMillis: Long = 0L
    private var resumeStartMillis: Long = 0L
    private var recordingStartMillis: Long = 0L


    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startCountdownAndRecording() {
        if (recordingState != RecordingState.STOPPED) return

        viewModelScope.launch {
            for (i in 3 downTo 1) {
                countdown = i
                delay(1000)
            }
            countdown = 0
            delay(1000)
            countdown = -1

            recordingStartMillis = System.currentTimeMillis()
            resumeStartMillis = System.currentTimeMillis()
            elapsedAccumulatedMillis = 0L
            recordingState = RecordingState.RECORDING

            startTimer()

            audioRecorder.start(
                scope = viewModelScope,
                onWaveformUpdate = { _waveform.value = it },
                onComplete = {
                    recordedFile = it
                    onRecordingComplete?.invoke(it)
                }
            )
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (recordingState == RecordingState.RECORDING) {
                val now = System.currentTimeMillis()
                val totalElapsed = (now - resumeStartMillis) + elapsedAccumulatedMillis
                val seconds = (totalElapsed / 1000).toInt()
                val minutes = seconds / 60
                val sec = seconds % 60
                elapsedTime = "%02d:%02d".format(minutes, sec)
                delay(1000)
            }
        }
    }

    fun toggleRecording() {
        when (recordingState) {
            RecordingState.RECORDING -> pauseRecording()
            RecordingState.PAUSED -> resumeRecording()
            else -> Unit
        }
    }

    private fun pauseRecording() {
        recordingState = RecordingState.PAUSED
        audioRecorder.pause()
        timerJob?.cancel()
        elapsedAccumulatedMillis += System.currentTimeMillis() - resumeStartMillis
    }

    private fun resumeRecording() {
        recordingState = RecordingState.RECORDING
        audioRecorder.resume()
        resumeStartMillis = System.currentTimeMillis()
        startTimer()
    }

    fun stopRecording(): Boolean {
        val duration = System.currentTimeMillis() - recordingStartMillis
        if (duration < 30000) return false

        recordingState = RecordingState.STOPPED
        timerJob?.cancel()
        audioRecorder.stop()
        return true
    }

    fun showConfirmDialog() {
        showStopConfirm = true
    }

    fun hideConfirmDialog() {
        showStopConfirm = false
    }
}

enum class RecordingState {
    STOPPED, RECORDING, PAUSED
}
