package com.ssafy.spico.domain.gpt.service

interface GptService {
    fun generateQuestions(speechContent: String?, count: Int): List<String>
}