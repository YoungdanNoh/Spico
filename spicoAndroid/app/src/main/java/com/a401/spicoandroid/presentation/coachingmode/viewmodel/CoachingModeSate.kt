package com.a401.spicoandroid.presentation.coachingmode.viewmodel

import com.a401.spicoandroid.common.presentation.BaseState

data class CoachingModeState(
    val countdown: Int = 3,
    val elapsedTime: String = "00:00",
    val showStopConfirm: Boolean = false,
    val waveform: List<Float> = emptyList(),
    val volumeFeedback: String? = null,
    val volumeScore: Int = 100,
    val pauseCount: Int = 0,
    val speedStatus: String = "MIDDLE",

    val isSuccess: Boolean,
    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    override val toastMessage: String? = null,
) : BaseState
