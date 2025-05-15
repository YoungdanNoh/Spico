package com.ssafy.spico.domain.randomSpeech.dto

import com.ssafy.spico.domain.randomSpeech.exception.RandomSpeechError
import com.ssafy.spico.domain.randomSpeech.exception.RandomSpeechException
import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech
import com.ssafy.spico.domain.randomSpeech.model.Topic

data class RandomSpeechDetailResponseDto(
    val title: String,
    val topic: Topic,
    val date: String,
    val question: String,
    val newsTitle: String,
    val newsUrl: String,
    val newsSummary: String,
    val feedback: String,
    val script: String
)

fun RandomSpeech.toDetailResponse(): RandomSpeechDetailResponseDto {
    this.report?: throw RandomSpeechException(RandomSpeechError.REPORT_NOT_FOUND)
    this.content?: throw RandomSpeechException(RandomSpeechError.CONTENT_NOT_FOUND)
    return RandomSpeechDetailResponseDto(
        title = this.report.title,
        topic = this.topic,
        date = this.createdAt.toLocalDate().toString(),
        question = this.content.question,
        newsTitle = this.content.newsTitle,
        newsUrl = this.content.newsUrl,
        newsSummary = this.content.newsSummary,
        feedback = this.report.feedback,
        script = this.report.script,
    )
}