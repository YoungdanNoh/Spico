package com.ssafy.spico.domain.landingPage.controller

import com.ssafy.spico.common.annotaion.UserId
import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.landingPage.dto.LandingPageReportsResponseDto
import com.ssafy.spico.domain.landingPage.service.LandingPageReportService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/landingPage")
class LandingPageController(
    private val landingPageReportService: LandingPageReportService
) {

    @GetMapping("/reports")
    fun landingPageReports(
        @UserId userId: Int
    ): ApiResponse<LandingPageReportsResponseDto> {

        return ApiResponse.success(landingPageReportService.landingPageReports(userId))
    }

}