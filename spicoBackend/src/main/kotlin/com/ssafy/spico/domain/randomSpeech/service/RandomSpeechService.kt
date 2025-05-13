package com.ssafy.spico.domain.randomSpeech.service

interface RandomSpeechService {
    fun startRandomSpeech()
    fun endRandomSpeech()
    fun getRandomSpeechList()
    fun getRandomSpeechDetail()
    fun deleteRandomSpeech()
}