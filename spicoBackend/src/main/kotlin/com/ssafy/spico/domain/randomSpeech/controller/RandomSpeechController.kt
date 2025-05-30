package com.ssafy.spico.domain.randomSpeech.controller

import com.ssafy.spico.common.annotaion.UserId
import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.randomSpeech.dto.*
import com.ssafy.spico.domain.randomSpeech.dto.gpt.RandomSpeechListResponseDto
import com.ssafy.spico.domain.randomSpeech.service.RandomSpeechService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/random-speeches")
class RandomSpeechController(
    private val randomSpeechService: RandomSpeechService
) {
    @GetMapping
    fun getRandomSpeeches(
        @UserId userId: Int
    ): ApiResponse<RandomSpeechListResponseDto> {
        val randomSpeeches = randomSpeechService.getRandomSpeechList(userId)
        return ApiResponse.success(RandomSpeechListResponseDto(randomSpeeches.map { it.toResponse() }))
    }

    @GetMapping("/{randomSpeechId}")
    fun getRandomSpeechDetail(
        @PathVariable randomSpeechId: Int,
        @UserId userId: Int
    ): ApiResponse<RandomSpeechDetailResponseDto> {
        val randomSpeech = randomSpeechService.getRandomSpeechDetail(userId, randomSpeechId)
        return ApiResponse.success(randomSpeech.toDetailResponse())
    }

    @PostMapping
    fun startRandomSpeech(
        @RequestBody request: CreateRandomSpeechRequestDto,
        @UserId userId: Int
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

    @DeleteMapping("{randomSpeechId}")
    fun deleteRandomSpeech(
        @PathVariable randomSpeechId: Int,
        @UserId userId: Int
    ): ApiResponse<Unit> {
        randomSpeechService.deleteRandomSpeech(userId, randomSpeechId)
        return ApiResponse.success()
    }
}