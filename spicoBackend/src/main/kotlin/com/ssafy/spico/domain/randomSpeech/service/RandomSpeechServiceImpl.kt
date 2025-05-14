package com.ssafy.spico.domain.randomSpeech.service

import com.ssafy.spico.domain.gpt.service.GptService
import com.ssafy.spico.domain.news.service.NewsService
import com.ssafy.spico.domain.randomSpeech.model.*
import com.ssafy.spico.domain.randomSpeech.repository.RandomSpeechRepository
import com.ssafy.spico.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RandomSpeechServiceImpl(
    private val randomSpeechRepository: RandomSpeechRepository,
    private val userRepository: UserRepository,
    private val newsService: NewsService,
    private val gptService: GptService
): RandomSpeechService {

    @Transactional
    override fun startRandomSpeech(randomSpeech: RandomSpeech): Content {
        val userEntity = userRepository.getReferenceById(randomSpeech.userId)
        val savedRandomSpeech = randomSpeechRepository.save(randomSpeech.toEntity(userEntity))

        val newsList = newsService.searchNewsByTopic(randomSpeech.topic)
        val selectedNews = newsList
            .filter { it.description.length >= 50 }
            .shuffled()
            .first()

        selectedNews.let{
            savedRandomSpeech.updateNews(UpdateNewsCommand(
                title = selectedNews.title,
                url = selectedNews.link,
                summary = selectedNews.description
            ))
        }

        val question = gptService.generateRandomSpeechQuestion(randomSpeech.topic, selectedNews)
        savedRandomSpeech.updateQuestion(UpdateQuestionCommand(question))

        return Content(
            id = savedRandomSpeech.randomSpeechId,
            newsTitle = selectedNews.title,
            newsUrl = selectedNews.link,
            newsSummary = selectedNews.description,
            question = question
        )
    }

    override fun endRandomSpeech() {
        TODO("Not yet implemented")
    }

    override fun getRandomSpeechList(userId: Int): List<RandomSpeech> {
        val entities = randomSpeechRepository.findRandomSpeechesByUserId(userId)
        return entities.map { it.toModel() }
    }

    override fun getRandomSpeechDetail() {
        TODO("Not yet implemented")
    }

    override fun deleteRandomSpeech() {
        TODO("Not yet implemented")
    }
}