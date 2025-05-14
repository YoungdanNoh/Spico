package com.ssafy.spico.domain.randomSpeech.exception

class RandomSpeechException(val error: RandomSpeechError) : RuntimeException(error.errorMsg)