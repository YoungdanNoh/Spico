package com.ssafy.spico.domain.randomSpeech.model

data class UpdateNewsCommand(
    val title: String,
    val url: String,
    val summary: String
)