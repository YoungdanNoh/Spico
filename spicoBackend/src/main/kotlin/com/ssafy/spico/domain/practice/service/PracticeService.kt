package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.practice.dto.FinalPracticeResponseDto
import com.ssafy.spico.domain.practice.model.FinalPracticeSetting

interface PracticeService {

    fun createFinalPractice(
        projectId: Int,
        userId: Int,
        request: FinalPracticeSetting
    ): ApiResponse<FinalPracticeResponseDto>
}