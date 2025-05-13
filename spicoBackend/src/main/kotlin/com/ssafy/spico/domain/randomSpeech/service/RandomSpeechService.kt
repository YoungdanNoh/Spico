package com.ssafy.spico.domain.randomSpeech.service

import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech

interface RandomSpeechService {
    fun startRandomSpeech()
    fun endRandomSpeech()
    fun getRandomSpeechList(userId: Int): List<RandomSpeech>
    fun getRandomSpeechDetail()
    fun deleteRandomSpeech()
}