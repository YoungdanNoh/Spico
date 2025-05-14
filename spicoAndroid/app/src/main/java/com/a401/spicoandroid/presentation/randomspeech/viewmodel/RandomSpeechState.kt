package com.a401.spicoandroid.presentation.randomspeech.viewmodel

data class RandomSpeechState(
    val topic: String? = null,
    val prepTime: Int = 60,
    val speakTime: Int = 120,
    val speechId: Int? = null,
    val question: String = "",
    val newsTitle: String = "",
    val newsUrl: String = "",
    val newsSummary: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
