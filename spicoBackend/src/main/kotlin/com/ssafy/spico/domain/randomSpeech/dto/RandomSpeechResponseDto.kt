package com.ssafy.spico.domain.randomSpeech.dto

import com.ssafy.spico.domain.randomSpeech.entity.Topic
import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech

data class RandomSpeechResponseDto(
    val id: Int,
    val topic: Topic,
    val title: String,
    val dateTime: String
)

fun RandomSpeech.toResponse(): RandomSpeechResponseDto {
    this.id ?: throw IllegalArgumentException("RandomSpeech id cannot be null")
    this.title ?: throw IllegalArgumentException("RandomSpeech title cannot be null")
    return RandomSpeechResponseDto(
        id = this.id,
        topic = this.topic,
        title = this.title,
        dateTime = this.createdAt.toString()
    )
}