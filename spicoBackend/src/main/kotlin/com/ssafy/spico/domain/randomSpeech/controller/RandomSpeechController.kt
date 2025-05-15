package com.ssafy.spico.domain.randomSpeech.controller

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.randomSpeech.dto.*
import com.ssafy.spico.domain.randomSpeech.service.RandomSpeechService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/{randomSpeechId}")
    fun getRandomSpeechDetail(
        @PathVariable randomSpeechId: Int
    ): ApiResponse<RandomSpeechDetailResponseDto> {
        val randomSpeech = randomSpeechService.getRandomSpeechDetail(userId, randomSpeechId)
        return ApiResponse.success(randomSpeech.toDetailResponse())
    }

    @PostMapping
    fun startRandomSpeech(
        @RequestBody request: CreateRandomSpeechRequestDto
    ): ApiResponse<RandomSpeechContentResponseDto> {
        val randomSpeech = request.toRandomSpeech(userId)
        val content = randomSpeechService.startRandomSpeech(randomSpeech)
        return ApiResponse.success(content.toResponse())
    }

    @PatchMapping("{randomSpeechId}")
    fun endRandomSpeech(
        @PathVariable randomSpeechId: Int,
        @RequestBody request: UpdateRandomSpeechRequestDto
    ): ApiResponse<Unit> {
        randomSpeechService.endRandomSpeech(randomSpeechId, request.script)
        return ApiResponse.success()
    }
}