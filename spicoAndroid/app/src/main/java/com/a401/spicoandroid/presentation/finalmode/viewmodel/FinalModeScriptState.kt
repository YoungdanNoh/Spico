package com.a401.spicoandroid.presentation.finalmode.viewmodel

data class FinalModeScriptState(
    val script: String? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)