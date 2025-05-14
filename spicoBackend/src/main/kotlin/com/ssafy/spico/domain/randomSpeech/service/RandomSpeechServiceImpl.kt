package com.ssafy.spico.domain.randomSpeech.service

import com.ssafy.spico.domain.gpt.service.GptService
import com.ssafy.spico.domain.news.service.NewsService
import com.ssafy.spico.domain.randomSpeech.dto.gpt.GptFeedbackRequest
import com.ssafy.spico.domain.randomSpeech.exception.RandomSpeechError
import com.ssafy.spico.domain.randomSpeech.exception.RandomSpeechException
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

    @Transactional
    override fun endRandomSpeech(randomSpeechId: Int, script: String) {
        val randomSpeech = randomSpeechRepository.findById(randomSpeechId)
            .orElseThrow { RandomSpeechException(RandomSpeechError.SPEECH_NOT_FOUND) }

        randomSpeech.script?.takeIf { it.isNotBlank() }
            ?.let { throw RandomSpeechException(RandomSpeechError.ALREADY_ENDED_SPEECH) }

        val gptRequest = GptFeedbackRequest(
            question = randomSpeech.question,
            newsSummary = randomSpeech.newsSummary,
            script = script
        )

        val gptResponse = gptService.generateRandomSpeechFeedback(gptRequest)

        val command = UpdateResultCommand(
            script = script,
            feedback = gptResponse.feedback,
            title = gptResponse.title
        )

        randomSpeech.updateReport(command)
    }

    override fun getRandomSpeechList(userId: Int): List<RandomSpeech> {
        val entities = randomSpeechRepository.findRandomSpeechesByUserId(userId)
        return entities.map { it.toModel() }
    }

    override fun getRandomSpeechDetail() {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun deleteRandomSpeech() {
        TODO("Not yet implemented")
    }
}