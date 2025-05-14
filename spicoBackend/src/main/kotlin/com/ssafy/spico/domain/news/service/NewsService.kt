package com.ssafy.spico.domain.news.service

import com.ssafy.spico.domain.news.dto.NewsResponse
import com.ssafy.spico.domain.news.model.News
import com.ssafy.spico.domain.randomSpeech.model.Topic

interface NewsService {
    fun searchNewsByTopic(topic: Topic): List<News>
}