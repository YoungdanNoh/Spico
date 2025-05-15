package com.a401.spicoandroid.presentation.finalmode.viewmodel

import com.a401.spicoandroid.domain.finalmode.model.FinalQuestion

data class FinalQuestionState(
    val questions: List<FinalQuestion> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
