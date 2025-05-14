package com.ssafy.spico.domain.practice.service

import com.ssafy.spico.domain.practice.dto.*

interface CoachingPracticeReportService {

    fun coachingPracticeReport(
        projectId: Int,
        practiceId: Int
    ): CoachingPracticeReportResponseDto

}