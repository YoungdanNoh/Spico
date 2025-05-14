package com.ssafy.spico.domain.news.service

import com.ssafy.spico.domain.news.dto.NewsResponse
import com.ssafy.spico.domain.news.dto.toModel
import com.ssafy.spico.domain.news.model.News
import com.ssafy.spico.domain.randomSpeech.model.Topic
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class NewServiceImpl(
    @Value("\${news-api-key}")
    private val apiKey: String
): NewsService {

    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://newsdata.io/api/1/latest")
        .defaultHeader("Content-Type", "application/json")
        .build()

    override fun searchNewsByTopic(topic: Topic): List<News> {
        val category = topic.name.lowercase()
        val apiResponse: NewsResponse? = webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .queryParam("country", "kr")
                        .queryParam("category", category)
                        .queryParam("apikey", apiKey)
                        .build()
                }
                .retrieve()
                .bodyToMono(NewsResponse::class.java)
                .block()
        return apiResponse?.toModel() ?: emptyList()
    }
}