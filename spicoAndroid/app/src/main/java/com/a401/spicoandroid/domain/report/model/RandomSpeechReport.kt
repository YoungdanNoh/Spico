package com.a401.spicoandroid.domain.report.model

data class RandomSpeechReport(
    val title: String,
    val topic: String,
    val date: String,
    val question: String,
    val newsTitle: String,
    val newsUrl: String,
    val newsSummary: String,
    val feedback: String,
    val script: String
)
