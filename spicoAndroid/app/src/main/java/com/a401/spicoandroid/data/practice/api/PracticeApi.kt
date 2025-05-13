package com.a401.spicoandroid.data.practice.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PracticeApi {
    @POST("/api/projects/{projectId}/practices/coaching")
    suspend fun createCoachingPractice(
        @Path("projectId") projectId: Int
    ): Response<ApiResponse<PracticeIdResponse>>

    @POST("/api/projects/{projectId}/practices/final")
    suspend fun createFinalPractice(
        @Path("projectId") projectId: Int,
        @Body request: FinalPracticeRequest
    ): Response<ApiResponse<PracticeIdResponse>>
}