package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.*

interface FinalPracticeReportService {

    fun finalPracticeReport(
        userId: Int,
        projectId: Int,
        practiceId: Int
    ): FinalPracticeReportResponseDto

}