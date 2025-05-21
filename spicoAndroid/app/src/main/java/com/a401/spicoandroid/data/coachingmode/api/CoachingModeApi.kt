package com.a401.spicoandroid.data.coachingmode.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.coachingmode.dto.CoachingResultRequestDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CoachingModeApi {
    @POST("projects/{projectId}/practices/coaching/{practiceId}/result")
    suspend fun postCoachingResult(
        @Path("projectId") projectId: Int,
        @Path("practiceId") practiceId: Int,
        @Body request: CoachingResultRequestDto
    ): ApiResponse<Unit>
}