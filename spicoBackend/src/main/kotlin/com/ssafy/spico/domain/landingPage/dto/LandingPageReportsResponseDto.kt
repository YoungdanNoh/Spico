package com.ssafy.spico.domain.landingPage.dto

import com.ssafy.spico.domain.landingPage.model.LandingPageReports
import com.ssafy.spico.domain.landingPage.model.Reports


data class LandingPageReportsResponseDto(
    val reports: List<Reports>?

)

fun LandingPageReports.toResponse(): LandingPageReportsResponseDto {
    return LandingPageReportsResponseDto(
        reports = this.reports
    )
}