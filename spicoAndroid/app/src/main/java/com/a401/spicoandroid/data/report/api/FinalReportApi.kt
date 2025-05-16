package com.a401.spicoandroid.data.report.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.report.dto.FinalReportDto
import retrofit2.http.*

interface FinalReportApi {
    @GET("projects/{projectId}/practices/final/{practiceId}")
    suspend fun getFinalReport(
        @Path("projectId") projectId: Int,
        @Path("practiceId") practiceId: Int
    ): ApiResponse<FinalReportDto>
}
