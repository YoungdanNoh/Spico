package com.ssafy.spico.domain.landingPage.service

import com.ssafy.spico.domain.landingPage.dto.LandingPageReportsResponseDto

interface LandingPageReportService {

    fun landingPageReports(userId: Int) : LandingPageReportsResponseDto
}