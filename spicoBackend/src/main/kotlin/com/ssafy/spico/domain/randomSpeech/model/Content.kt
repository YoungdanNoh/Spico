package com.ssafy.spico.domain.randomSpeech.model

data class Content(
    val id: Int,
    val newsTitle: String,
    val newsUrl: String,
    val newsSummary: String,
    val question: String
)
