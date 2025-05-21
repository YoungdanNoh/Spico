package com.a401.spicoandroid.data.randomspeech.dto

import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechSummary

data class RandomSpeechSummaryDto(
    val id: Int,
    val topic: String,
    val title: String,
    val dateTime: String
)

data class RandomSpeechListResponseDto(
    val randomSpeeches: List<RandomSpeechSummaryDto>
) {
    fun toDomain(): List<RandomSpeechSummary> {
        return randomSpeeches.map {
            RandomSpeechSummary(it.id, it.topic, it.title, it.dateTime)
        }
    }
}
