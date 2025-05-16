package com.a401.spicoandroid.presentation.finalmode.viewmodel

data class FinalModeResultState(
    val presignedUrl: String? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)