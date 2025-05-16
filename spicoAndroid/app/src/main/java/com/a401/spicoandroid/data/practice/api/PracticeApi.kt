package com.a401.spicoandroid.data.practice.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.practice.dto.CoachingPracticeResponseDto
import com.a401.spicoandroid.data.practice.dto.FinalPracticeRequest
import com.a401.spicoandroid.data.practice.dto.FinalSettingResponseDto
import com.a401.spicoandroid.data.practice.dto.PracticeDto
import com.a401.spicoandroid.data.practice.dto.PracticeIdResponse
import com.a401.spicoandroid.data.project.dto.PracticeListResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PracticeApi {
    @POST("projects/{projectId}/practices/coaching")
    suspend fun createCoachingPractice(
        @Path("projectId") projectId: Int
    ): ApiResponse<CoachingPracticeResponseDto>

    @POST("projects/{projectId}/practices/final")
    suspend fun createFinalPractice(
        @Path("projectId") projectId: Int,
        @Body request: FinalPracticeRequest
    ): ApiResponse<PracticeIdResponse>

    @GET("projects/{projectId}/practices")
    suspend fun getPracticeList(
        @Path("projectId") projectId: Int,
        @Query("practice-filter") filter: String? = null,
        @Query("cursor") cursor: Int?,
        @Query("size") size: Int
    ): ApiResponse<PracticeListResponseDto>

    @GET("/api/users/me/final-settings")
    suspend fun getFinalSetting(
    ): ApiResponse<FinalSettingResponseDto>

    @PATCH("/api/users/me/final-settings")
    suspend fun saveFinalSetting(
        @Body request: FinalPracticeRequest
    ): ApiResponse<FinalSettingResponseDto>

    @DELETE("projects/{projectId}/practices/{practiceId}")
    suspend fun deletePractice(
        @Path("projectId") projectId: Int,
        @Path("practiceId") practiceId: Int
    ): ApiResponse<Unit>
}