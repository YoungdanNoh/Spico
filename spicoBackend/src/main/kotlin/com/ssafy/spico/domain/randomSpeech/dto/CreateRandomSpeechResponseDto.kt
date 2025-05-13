package com.ssafy.spico.domain.randomSpeech.dto

import com.ssafy.spico.domain.randomSpeech.model.Content

data class RandomSpeechContentResponseDto(
    val id: Int,
    val question: String,
    val newsTitle: String,
    val newsUrl: String,
    val newsSummary: String
)

fun Content.toResponse(): RandomSpeechContentResponseDto {
    return RandomSpeechContentResponseDto(
        id = id,
        question = question,
        newsTitle = newsTitle,
        newsUrl = newsUrl,
        newsSummary = newsSummary
    )
}