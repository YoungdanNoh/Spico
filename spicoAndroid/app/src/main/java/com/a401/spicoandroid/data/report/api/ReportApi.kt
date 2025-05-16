package com.a401.spicoandroid.data.report.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.report.dto.CoachingReportDto
import com.a401.spicoandroid.data.report.dto.RandomSpeechReportDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ReportApi {
    @GET("random-speeches/{randomSpeechId}")
    suspend fun getRandomSpeechReport(
        @Path("randomSpeechId") id: Int
    ): ApiResponse<RandomSpeechReportDto>

    @DELETE("random-speeches/{randomSpeechId}")
    suspend fun deleteRandomSpeech(@Path("randomSpeechId") id: Int): ApiResponse<Unit>

    @GET("projects/{projectId}/practices/coaching/{practiceId}")
    suspend fun getCoachingReport(
        @Path("projectId") projectId: Int,
        @Path("practiceId") practiceId: Int
    ): ApiResponse<CoachingReportDto>

}
