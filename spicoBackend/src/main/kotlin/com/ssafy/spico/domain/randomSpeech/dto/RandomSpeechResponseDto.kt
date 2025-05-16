package com.ssafy.spico.domain.randomSpeech.dto

import com.ssafy.spico.domain.randomSpeech.model.Topic
import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech
import java.time.format.DateTimeFormatter

data class RandomSpeechResponseDto(
    val id: Int,
    val topic: Topic,
    val title: String,
    val dateTime: String
)

fun RandomSpeech.toResponse(): RandomSpeechResponseDto {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    this.id ?: throw IllegalArgumentException("RandomSpeech id cannot be null")
    this.content ?: throw IllegalArgumentException("RandomSpeech content cannot be null")
    return RandomSpeechResponseDto(
        id = this.id,
        topic = this.topic,
        title = this.content.newsTitle,
        dateTime = this.createdAt.format(formatter)
    )
}