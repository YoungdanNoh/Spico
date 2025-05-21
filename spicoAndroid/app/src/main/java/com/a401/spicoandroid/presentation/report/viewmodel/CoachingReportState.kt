package com.a401.spicoandroid.presentation.report.viewmodel

import com.a401.spicoandroid.common.presentation.BaseState

data class CoachingReportState(
    val projectName: String = "",
    val roundCount: Int = 0,
    val recordUrl: String = "",
    val volumeStatus: String = "",
    val speedStatus: String = "",
    val pauseCount: Int = 0,
    val pronunciationStatus: String = "",

    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    override val toastMessage: String? = null,
) : BaseState