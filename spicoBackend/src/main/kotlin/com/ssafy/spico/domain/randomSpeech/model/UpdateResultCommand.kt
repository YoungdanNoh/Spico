package com.ssafy.spico.domain.randomSpeech.model

data class UpdateResultCommand(
    val script: String,
    val feedback: String,
    val title: String
)