package com.ssafy.spico.domain.randomSpeech.repository

import com.ssafy.spico.domain.randomSpeech.entity.RandomSpeechEntity

interface RandomSpeechRepositoryCustom {
    fun findRandomSpeechesByUserId(userId: Int): List<RandomSpeechEntity>
}