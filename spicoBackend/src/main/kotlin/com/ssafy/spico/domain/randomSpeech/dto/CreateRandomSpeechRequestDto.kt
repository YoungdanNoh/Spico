package com.ssafy.spico.domain.randomSpeech.dto

import com.ssafy.spico.domain.randomSpeech.model.Topic
import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech
import java.time.LocalDateTime

data class CreateRandomSpeechRequestDto(
    val topic: Topic,
    val preparationTime: Int,
    val speechTime: Int
)

fun CreateRandomSpeechRequestDto.toRandomSpeech(userId: Int): RandomSpeech {
    return RandomSpeech(
        userId = userId,
        topic = topic,
        createdAt = LocalDateTime.now(),
        speechTime = speechTime,
        preparationTime = preparationTime,
        content = null,
        report = null
    )
}