package com.ssafy.spico.domain.news.dto

import com.ssafy.spico.domain.news.model.News

data class NewsResponse(
    val status: String?,
    val results: List<NewsArticle>?
)

data class NewsArticle(
    val title: String?,
    val description: String?,
    val link: String?,
    val pubDate: String?,
    val language: String?
)

fun NewsArticle.toModel(): News {
    return News(
        title = title.orEmpty(),
        link = link.orEmpty(),
        description = description.orEmpty(),
        language = language.orEmpty()
    )
}

fun NewsArticle.isValid(): Boolean {
    return !description.isNullOrBlank() && language == "korean"
}

fun NewsResponse.toModel(): List<News> {
    return results.orEmpty()
        .filter { it.isValid() }
        .map { it.toModel() }
}

fun NewsResponse.pickRandomSpeechCandidate(): News {
    return results.orEmpty()
        .filter { it.isValid() }
        .filter { (it.description?.length ?: 0) >= 50 }
        .filter { it.title != it.description }
        .sortedByDescending { it.pubDate }
        .map { it.toModel() }
        .shuffled()
        .first()
}