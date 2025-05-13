package com.ssafy.spico.domain.randomSpeech.controller

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.randomSpeech.dto.*
import com.ssafy.spico.domain.randomSpeech.service.RandomSpeechService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/random-speeches")
class RandomSpeechController(
    private val randomSpeechService: RandomSpeechService,
    @Value("\${user-id}") private val userId: Int
) {
    @GetMapping
    fun getRandomSpeeches(): ApiResponse<List<RandomSpeechResponseDto>> {
        val randomSpeeches = randomSpeechService.getRandomSpeechList(userId)
        return ApiResponse.success(randomSpeeches.map { it.toResponse() })
    }

    @PostMapping
    fun createRandomSpeech(
        @RequestBody request: CreateRandomSpeechRequestDto
    ): ApiResponse<RandomSpeechContentResponseDto> {
        val randomSpeech = request.toRandomSpeech(userId)
        val content = randomSpeechService.startRandomSpeech(randomSpeech)
        return ApiResponse.success(content.toResponse())
    }
}