package com.ssafy.spico.domain.randomSpeech.service

import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech
import com.ssafy.spico.domain.randomSpeech.model.toModel
import com.ssafy.spico.domain.randomSpeech.repository.RandomSpeechRepository
import org.springframework.stereotype.Service

@Service
class RandomSpeechServiceImpl(
    private val randomSpeechRepository: RandomSpeechRepository
): RandomSpeechService {
    override fun startRandomSpeech() {
        TODO("Not yet implemented")
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