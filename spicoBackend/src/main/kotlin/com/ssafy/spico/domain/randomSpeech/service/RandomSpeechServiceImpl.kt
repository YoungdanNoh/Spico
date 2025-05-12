package com.ssafy.spico.domain.randomSpeech.service

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

    override fun getRandomSpeechList() {
        TODO("Not yet implemented")
    }

    override fun getRandomSpeechDetail() {
        TODO("Not yet implemented")
    }

    override fun deleteRandomSpeech() {
        TODO("Not yet implemented")
    }
}