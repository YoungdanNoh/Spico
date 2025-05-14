package com.ssafy.spico.domain.randomSpeech.dto

import com.ssafy.spico.domain.randomSpeech.model.Topic
import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech

data class RandomSpeechResponseDto(
    val id: Int,
    val topic: Topic,
    val title: String,
    val dateTime: String
)

fun RandomSpeech.toResponse(): RandomSpeechResponseDto {
    this.id ?: throw IllegalArgumentException("RandomSpeech id cannot be null")
    this.content ?: throw IllegalArgumentException("RandomSpeech content cannot be null")
    return RandomSpeechResponseDto(
        id = this.id,
        topic = this.topic,
        title = this.content.newsTitle,
        dateTime = this.createdAt.toString()
    )
}