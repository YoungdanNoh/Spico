package com.a401.spicoandroid.data.finalmode.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.finalmode.dto.*
import retrofit2.http.*

interface FinalModeApi {
    // Q&A 질문 생성
    @POST("projects/{projectId}/practices/final/{practiceId}/qa")
    suspend fun postFinalQuestions(
        @Path("projectId") projectId: Int,
        @Path("practiceId") practiceId: Int,
        @Body request: SpeechContentRequest
    ): ApiResponse<FinalQuestionResponseDto>

    // 결과 제출 + presigned URL 응답
    @POST("projects/{projectId}/practices/final/{practiceId}/result")
    suspend fun finishFinalPractice(
        @Path("projectId") projectId: Int,
        @Path("practiceId") practiceId: Int,
        @Body request: FinalModeResultRequestDto
    ): ApiResponse<FinalModeResultResponseDto>

}