package com.ssafy.spico.domain.practice.controller

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.practice.dto.FinalPracticeRequestDto
import com.ssafy.spico.domain.practice.dto.FinalPracticeResponseDto
import com.ssafy.spico.domain.practice.dto.toModel
import com.ssafy.spico.domain.practice.dto.toResponse
import com.ssafy.spico.domain.practice.model.FinalPracticeInfo
import com.ssafy.spico.domain.practice.service.PracticeServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects/{projectId}/practices")
class PracticeController(
    private val practiceService: PracticeServiceImpl,
    @Value("\${user-id}") private val userId: Int
) {

    @PostMapping("/final")
    fun createFinalPractice(
        @PathVariable projectId: Int,
        @RequestBody request: FinalPracticeRequestDto
    ): ApiResponse<FinalPracticeResponseDto> {
        val setting = request.toModel()

        return ApiResponse.success(practiceService.createFinalPractice(projectId, userId, setting))

    }
}