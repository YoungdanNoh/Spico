package com.ssafy.spico.domain.gpt.service

import com.ssafy.spico.domain.news.model.News
import com.ssafy.spico.domain.randomSpeech.dto.gpt.GptFeedbackRequest
import com.ssafy.spico.domain.randomSpeech.dto.gpt.GptFeedbackResponse
import com.ssafy.spico.domain.randomSpeech.model.Topic

interface GptService {
    fun generateQuestions(speechContent: String?, count: Int): List<String>
    fun generateRandomSpeechQuestion(topic: Topic, news: News): String
    fun generateRandomSpeechFeedback(request: GptFeedbackRequest): GptFeedbackResponse
}