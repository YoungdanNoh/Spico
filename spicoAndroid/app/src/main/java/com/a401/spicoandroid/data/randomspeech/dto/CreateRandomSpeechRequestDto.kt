package com.a401.spicoandroid.data.randomspeech.dto

data class CreateRandomSpeechRequestDto(
    val topic: String,
    val preparationTime: Int,
    val speechTime: Int
)
