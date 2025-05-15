package com.ssafy.spico.domain.randomSpeech.service

import com.ssafy.spico.domain.randomSpeech.model.Content
import com.ssafy.spico.domain.randomSpeech.model.RandomSpeech

interface RandomSpeechService {
    fun startRandomSpeech(randomSpeech: RandomSpeech): Content
    fun endRandomSpeech(randomSpeechId: Int, script: String)
    fun getRandomSpeechList(userId: Int): List<RandomSpeech>
    fun getRandomSpeechDetail(userId: Int, randomSpeechId: Int): RandomSpeech
    fun deleteRandomSpeech()
}