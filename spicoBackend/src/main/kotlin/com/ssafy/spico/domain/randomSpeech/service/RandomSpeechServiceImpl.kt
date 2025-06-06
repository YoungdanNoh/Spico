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

        val newsList = try {
            newsService.searchNewsByTopic(randomSpeech.topic)
        } catch (e: Exception) {
            throw RandomSpeechException(RandomSpeechError.NEWS_API_FAILED)
        }

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

        val question = try {
            gptService.generateRandomSpeechQuestion(randomSpeech.topic, selectedNews)
        } catch (e: Exception) {
            throw RandomSpeechException(RandomSpeechError.GPT_QUESTION_FAILED)
        }

        savedRandomSpeech.updateQuestion(UpdateQuestionCommand(
            question = question
        ))

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

        val gptResponse = try {
            gptService.generateRandomSpeechFeedback(gptRequest)
        } catch (e: Exception) {
            throw RandomSpeechException(RandomSpeechError.GPT_FEEDBACK_FAILED)
        }

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

    override fun getRandomSpeechDetail(userId: Int, randomSpeechId: Int): RandomSpeech {
        val randomSpeech = randomSpeechRepository.findById(randomSpeechId)
            .orElseThrow { RandomSpeechException(RandomSpeechError.SPEECH_NOT_FOUND) }

        randomSpeech.takeIf { it.userEntity.id == userId }
            ?: throw RandomSpeechException(RandomSpeechError.UNAUTHORIZED_ACCESS)

        return randomSpeech.toModel()
    }

    @Transactional
    override fun deleteRandomSpeech(userId: Int, randomSpeechId: Int) {
        val randomSpeech = randomSpeechRepository.findById(randomSpeechId)
            .orElseThrow { RandomSpeechException(RandomSpeechError.SPEECH_NOT_FOUND) }

        randomSpeech.takeIf { it.userEntity.id == userId }
            ?: throw RandomSpeechException(RandomSpeechError.UNAUTHORIZED_ACCESS)

        randomSpeechRepository.delete(randomSpeech)
    }
}