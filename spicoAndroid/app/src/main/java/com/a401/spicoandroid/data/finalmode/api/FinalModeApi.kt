package com.a401.spicoandroid.data.finalmode.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.finalmode.dto.FinalQuestionResponseDto
import com.a401.spicoandroid.data.finalmode.dto.SpeechContentRequest
import retrofit2.http.*

interface FinalModeApi {
    @POST("projects/{projectId}/practices/final/{practiceId}/qa")
    suspend fun postFinalQuestions(
        @Path("projectId") projectId: Int,
        @Path("practiceId") practiceId: Int,
        @Body request: SpeechContentRequest
    ): ApiResponse<FinalQuestionResponseDto>
}