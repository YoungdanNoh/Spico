package com.ssafy.spico.domain.randomSpeech.controller

import com.ssafy.spico.domain.randomSpeech.service.RandomSpeechService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/random-speeches")
class RandomSpeechController(
    private val randomSpeechService: RandomSpeechService,
    @Value("\${user-id}") private val userId: Int
) {

}