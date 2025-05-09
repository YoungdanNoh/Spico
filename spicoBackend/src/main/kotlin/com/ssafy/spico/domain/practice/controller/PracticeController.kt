package com.ssafy.spico.domain.practice.controller

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.service.PracticeServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects/{projectId}/practices")
class PracticeController(
    private val practiceService: PracticeServiceImpl,
    @Value("\${user-id}") private val userId: Int
) {

    // 파이널 모드 시작
    @PostMapping("/final")
    fun startFinalPractice(
        @PathVariable projectId: Int,
        @RequestBody request: StartFinalPracticeRequestDto
    ): ApiResponse<StartFinalPracticeResponseDto> {
        val setting = request.toModel()

        return ApiResponse.success(practiceService.startFinalPractice(userId, projectId, setting))

    }

    // 파이널 모드 종료 -> GPT 질문 생성
    @PostMapping("/final/{practiceId}/qa")
    fun endFinalPractice(
        @PathVariable projectId: Int,
        @PathVariable practiceId: Int,
        @RequestBody request: EndFinalPracticeRequestDto
    ): ApiResponse<EndFinalPracticeResponseDto> {
        val speech = request.toModel()

        return ApiResponse.success(practiceService.endFinalPractice(userId, projectId, practiceId, speech))
    }

    // 코칭 모드 시작
    @PostMapping("/coaching")
    fun startCoachingPractice(
        @PathVariable projectId: Int
    ): ApiResponse<StartCoachingPracticeResponseDto> {

        return ApiResponse.success(practiceService.startCoachingPractice(userId, projectId))
    }

    // 파이널 모드 리포트 조회
    @GetMapping("/final/{practiceId}")
    fun finalPracticeReport(
        @PathVariable projectId: Int,
        @PathVariable practiceId: Int
    ): ApiResponse<FinalPracticeReportResponseDto> {

        return ApiResponse.success(practiceService.finalPracticeReport(userId, projectId, practiceId))
    }
}