package com.a401.spicoandroid.data.report.dto

import com.a401.spicoandroid.domain.report.model.RandomSpeechReport

data class RandomSpeechReportDto(
    val title: String,
    val topic: String,
    val date: String,
    val question: String,
    val newsTitle: String,
    val newsUrl: String,
    val newsSummary: String,
    val feedback: String,
    val script: String
) {
    fun toDomain(): RandomSpeechReport {
        return RandomSpeechReport(
            title = title,
            topic = topic,
            date = date,
            question = question,
            newsTitle = newsTitle,
            newsUrl = newsUrl,
            newsSummary = newsSummary,
            feedback = feedback,
            script = script
        )
    }
}
