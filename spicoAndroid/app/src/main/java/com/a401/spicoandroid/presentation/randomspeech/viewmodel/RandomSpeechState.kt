package com.a401.spicoandroid.presentation.randomspeech.viewmodel

import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechTopic

data class RandomSpeechState(
    val topic: RandomSpeechTopic? = null,
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
