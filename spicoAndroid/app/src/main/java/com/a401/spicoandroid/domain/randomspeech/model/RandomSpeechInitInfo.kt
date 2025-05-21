package com.a401.spicoandroid.domain.randomspeech.model

data class RandomSpeechInitInfo(
    val id: Int,
    val question: String,
    val newsTitle: String,
    val newsUrl: String,
    val newsSummary: String
)