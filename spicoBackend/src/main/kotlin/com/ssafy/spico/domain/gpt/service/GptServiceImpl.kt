package com.ssafy.spico.domain.gpt.service

import com.ssafy.spico.domain.gpt.dto.OpenAiResponse
import com.ssafy.spico.domain.practice.exception.GPTError
import com.ssafy.spico.domain.practice.exception.GPTException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GptServiceImpl(
    @Value("\${openai-api-key}")
    private val apiKey: String,

) : GptService {

    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://api.openai.com/v1/chat/completions")
        .defaultHeader("Authorization", "Bearer $apiKey")
        .defaultHeader("Content-Type", "application/json")
        .build()

    override fun generateQuestions(speechContent: String?, count: Int): List<String> {

        // STT 데이터가 없으면 예외 처리
        if (speechContent.isNullOrBlank()) {
            throw GPTException(GPTError.INVALID_INPUT)
        }

        val prompt = """
            I just gave a presentation with the following content:
            
            "$speechContent"
            
            Based on that, generate exactly $count diverse and relevant questions an audience might ask me. 
            Return only a JSON array of plain questions without numbering or any explanation.
        """.trimIndent()

        val requestBody = mapOf(
            "model" to "gpt-4",
            "messages" to listOf(
                mapOf("role" to "user", "content" to prompt)
            ),
            "temperature" to 0.7
        )

        val response = try {
            webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(OpenAiResponse::class.java)
                .block()
        } catch (e: Exception) {
            throw GPTException(GPTError.GPT_GENERATION_ERROR)
        }

        val content = response?.choices?.firstOrNull()?.message?.content
            ?: throw GPTException(GPTError.GPT_EMPTY_RESPONSE)

        println("GPT 응답: $content")
        return parseJsonArray(content)
    }

    // 응답 문자열을 List<String>으로 파싱 (ex: ["Q1", "Q2", ...])
    private fun parseJsonArray(json: String): List<String> {
        return try {
            val regex = Regex("\"(.*?)\"")
            regex.findAll(json).map { it.groupValues[1] }.toList()
        } catch (e: Exception) {
            throw GPTException(GPTError.GPT_PARSING_ERROR)
        }
    }
}