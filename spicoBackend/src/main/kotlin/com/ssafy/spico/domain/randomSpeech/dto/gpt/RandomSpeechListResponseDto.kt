package com.ssafy.spico.domain.randomSpeech.dto.gpt

import com.ssafy.spico.domain.randomSpeech.dto.RandomSpeechResponseDto

data class RandomSpeechListResponseDto(
    val randomSpeeches: List<RandomSpeechResponseDto>
)
