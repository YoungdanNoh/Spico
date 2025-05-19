package com.ssafy.spico.domain.gpt.service

import com.ssafy.spico.domain.gpt.dto.OpenAiResponse
import com.ssafy.spico.domain.news.model.News
import com.ssafy.spico.domain.practice.exception.GPTError
import com.ssafy.spico.domain.practice.exception.GPTException
import com.ssafy.spico.domain.randomSpeech.dto.gpt.GptFeedbackRequest
import com.ssafy.spico.domain.randomSpeech.dto.gpt.GptFeedbackResponse
import com.ssafy.spico.domain.randomSpeech.model.Topic
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

    override fun generateRandomSpeechQuestion(topic: Topic, news: News): String {
        val prompt = """
        You are a coach helping users prepare for impromptu speeches and presentation-style interviews based on current events.
        Given the news article below as background material, generate one thoughtful and natural question in Korean that an audience member might ask after a short speech on the given topic.
        The question should not be limited to the specific incident in the article, but should instead invite broader thinking and discussion related to the general theme.
        Aim to help the user prepare for realistic, insightful audience engagement.
        
        Topic: ${topic.name}
        
        News Title: ${news.title}
        
        News Summary: ${news.description}
        
        Please output **only one Korean question**, without any explanation or additional text.
    """.trimIndent()

        val requestBody = mapOf(
            "model" to "gpt-4",
            "messages" to listOf(
                mapOf("role" to "user", "content" to prompt)
            ),
            "temperature" to 0.8
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

        return response?.choices?.firstOrNull()?.message?.content
            ?.trim()
            ?: throw GPTException(GPTError.GPT_EMPTY_RESPONSE)
    }

    override fun generateRandomSpeechFeedback(request: GptFeedbackRequest): GptFeedbackResponse {
        val prompt = """
        You are a professional speaking coach helping users improve their impromptu speeches.
        
        The user has been given a question and prepared a short speech using the following news article as background material.
        
        Please carefully read the information below and generate a helpful response.
        
        News Summary (Korean): "${request.newsSummary}"
        
        Audience Question (Korean): "${request.question}"
        
        User Speech (Korean): "${request.script}"
        
        Your task is to provide the following information **in Korean**, with the title and feedback **separated by '|||'** as follows:
        
        - title: Write a single-line Korean title that summarizes the question and speech.
        - feedback: Provide warm and supportive feedback in Korean. Include 3 to 5 sentences that point out what was done well and what can be improved.
        
        Output format:
        Please return **only the title and feedback** separated by '|||' and in the following format:
            title ||| feedback
        Do NOT include any labels like "Title(제목):" or "Feedback(피드백):". Use only the format above.
        """.trimIndent()

        val requestBody = mapOf(
            "model" to "gpt-4",
            "messages" to listOf(
                mapOf("role" to "user", "content" to prompt)
            ),
            "temperature" to 0.8
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
            ?.trim()
            ?: throw GPTException(GPTError.GPT_EMPTY_RESPONSE)

        val parts = content.split("|||").map { it.trim() }
        if (parts.size != 2) {
            throw GPTException(GPTError.GPT_PARSING_ERROR)
        }

        return GptFeedbackResponse(
            title = parts[0],
            feedback = parts[1]
        )
    }
}