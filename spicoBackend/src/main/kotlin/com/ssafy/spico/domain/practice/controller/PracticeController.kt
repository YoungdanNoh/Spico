package com.ssafy.spico.domain.practice.controller

import com.ssafy.spico.common.annotaion.UserId
import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.practice.dto.*
import com.ssafy.spico.domain.practice.service.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects/{projectId}/practices")
class PracticeController(
    private val coachingPracticeService: CoachingPracticeService,
    private val finalPracticeService: FinalPracticeService,
    private val finalPracticeReportService: FinalPracticeReportService,
    private val coachingPracticeReportService: CoachingPracticeReportService,
    private val deletePracticeService: DeletePracticeService
) {

    // 연습 삭제
    @DeleteMapping("/{practiceId}")
    fun deletePractice(
        @PathVariable("practiceId") practiceId: Int
    ): ApiResponse<Unit>  {

        deletePracticeService.deletePractice(practiceId)

        return ApiResponse.success()
    }

    // 파이널 모드 시작
    @PostMapping("/final")
    fun startFinalPractice(
        @UserId userId: Int,
        @PathVariable projectId: Int,
        @RequestBody request: StartFinalPracticeRequestDto
    ): ApiResponse<StartFinalPracticeResponseDto> {

        val setting = request.toModel()

        return ApiResponse.success(finalPracticeService.startFinalPractice(userId, projectId, setting))

    }

    // 파이널 모드 종료 -> GPT 질문 생성
    @PostMapping("/final/{practiceId}/qa")
    fun generateGPTQuestion(
        @UserId userId: Int,
        @PathVariable projectId: Int,
        @PathVariable practiceId: Int,
        @RequestBody request: GenerateGPTQuestionRequestDto
    ): ApiResponse<GenerateGPTQuestionResponseDto> {

        val speech = request.toModel()

        return ApiResponse.success(finalPracticeService.generateGPTQuestion(userId, projectId, practiceId, speech))
    }

    // 파이널 모드 종료
    @PostMapping("/final/{practiceId}/result")
    fun endFinalPractice(
        @PathVariable projectId: Int,
        @PathVariable practiceId: Int,
        @RequestBody request: EndFinalPracticeRequestDto
    ): ApiResponse<EndFinalPracticeResponseDto>{

        val endFinalPractice = request.toModel()

        return ApiResponse.success(finalPracticeService.endFinalPractice(projectId, practiceId, endFinalPractice))
    }

    // 코칭 모드 시작
    @PostMapping("/coaching")
    fun startCoachingPractice(
        @UserId userId: Int,
        @PathVariable projectId: Int
    ): ApiResponse<StartCoachingPracticeResponseDto> {

        return ApiResponse.success(coachingPracticeService.startCoachingPractice(userId, projectId))
    }

    // 코칭모드 종료
    @PostMapping("/coaching/{practiceId}/result")
    fun endCoachingPractice(
        @UserId userId: Int,
        @PathVariable projectId: Int,
        @PathVariable practiceId: Int,
        @RequestBody request: EndCoachingPracticeRequestDto
    ): ApiResponse<EndCoachingPracticeResponseDto> {

        val endCoachingPractice = request.toModel()

        return ApiResponse.success(coachingPracticeService.endCoachingPractice(userId, projectId, practiceId, endCoachingPractice))
    }

    // 파이널 모드 리포트 조회
    @GetMapping("/final/{practiceId}")
    fun finalPracticeReport(
        @UserId userId: Int,
        @PathVariable projectId: Int,
        @PathVariable practiceId: Int
    ): ApiResponse<FinalPracticeReportResponseDto> {

        // TODO: videoUrl 응답 수정(minio에서 get 해와야 함)
        return ApiResponse.success(finalPracticeReportService.finalPracticeReport(userId, projectId, practiceId))
    }

    // 코칭 모드 리포트 조회
    @GetMapping("/coaching/{practiceId}")
    fun coachingPracticeReport(
        @PathVariable projectId: Int,
        @PathVariable practiceId: Int
    ): ApiResponse<CoachingPracticeReportResponseDto> {

        return ApiResponse.success(coachingPracticeReportService.coachingPracticeReport(projectId, practiceId))
    }
}