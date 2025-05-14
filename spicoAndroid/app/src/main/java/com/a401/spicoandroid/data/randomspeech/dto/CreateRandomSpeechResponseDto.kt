package com.a401.spicoandroid.data.randomspeech.dto

import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechInitInfo

data class CreateRandomSpeechResponseDto(
    val id: Int,
    val question: String,
    val newsTitle: String,
    val newsUrl: String,
    val newsSummary: String
) {
    fun toDomain(): RandomSpeechInitInfo {
        return RandomSpeechInitInfo(
            id = id,
            question = question,
            newsTitle = newsTitle,
            newsUrl = newsUrl,
            newsSummary = newsSummary
        )
    }
}
