package com.a401.spicoandroid.data.home.api

import com.a401.spicoandroid.data.home.dto.HomeReportResponseDto
import retrofit2.http.GET

interface HomeApi {
    @GET("/api/landingPage/reports")
    suspend fun getRecentReports(): HomeReportResponseDto
}
