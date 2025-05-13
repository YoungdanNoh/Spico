package com.ssafy.spico.domain.randomSpeech.model

import com.ssafy.spico.domain.randomSpeech.entity.RandomSpeechEntity
import com.ssafy.spico.domain.randomSpeech.entity.Topic
import com.ssafy.spico.domain.user.entity.UserEntity
import java.time.LocalDateTime

data class RandomSpeech(
    val id: Int? = null,
    val userId: Int,
    val topic: Topic,
    val createdAt: LocalDateTime,
    val speechTime: Int,
    val preparationTime: Int,
    val newsTitle: String?,
    val newsUrl: String?,
    val newSummary: String?,
    val question: String?,
    val feedback: String?,
    val script: String?,
    val title: String?
)

fun RandomSpeech.toEntity(userEntity: UserEntity): RandomSpeechEntity {
    return RandomSpeechEntity(
        userEntity,
        topic,
        speechTime,
        preparationTime,
        createdAt
    )
}

fun RandomSpeechEntity.toModel(): RandomSpeech {
    return RandomSpeech(
        id = this.randomSpeechId,
        userId = this.userEntity.id,
        topic = this.topic,
        createdAt = this.createdAt,
        speechTime = this.speechTime,
        preparationTime = this.preparationTime,
        newsTitle = this.newsTitle,
        newsUrl = this.newsUrl,
        newSummary = this.newsSummary,
        question = this.question,
        feedback = this.aiFeedback,
        script = this.script,
        title = this.aiTitle
    )
}