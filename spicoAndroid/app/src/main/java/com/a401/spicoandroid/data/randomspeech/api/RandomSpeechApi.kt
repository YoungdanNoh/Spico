package com.a401.spicoandroid.data.randomspeech.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.randomspeech.dto.CreateRandomSpeechRequestDto
import com.a401.spicoandroid.data.randomspeech.dto.CreateRandomSpeechResponseDto
import com.a401.spicoandroid.data.randomspeech.dto.SubmitRandomSpeechScriptRequestDto
import com.a401.spicoandroid.data.randomspeech.dto.RandomSpeechListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RandomSpeechApi {

    @POST("random-speeches")
    suspend fun createRandomSpeech(
        @Body request: CreateRandomSpeechRequestDto
    ): ApiResponse<CreateRandomSpeechResponseDto>

    @PATCH("random-speeches/{randomSpeechId}")
    suspend fun submitRandomSpeechScript(
        @Path("randomSpeechId") id: Int,
        @Body request: SubmitRandomSpeechScriptRequestDto
    ): ApiResponse<Unit>

    @GET("random-speeches")
    suspend fun getRandomSpeechList(): ApiResponse<RandomSpeechListResponseDto>
}
