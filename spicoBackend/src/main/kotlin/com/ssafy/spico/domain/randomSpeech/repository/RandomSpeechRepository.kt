package com.ssafy.spico.domain.randomSpeech.repository

import com.ssafy.spico.domain.randomSpeech.entity.RandomSpeechEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RandomSpeechRepository : JpaRepository<RandomSpeechEntity, Int>, RandomSpeechRepositoryCustom {}