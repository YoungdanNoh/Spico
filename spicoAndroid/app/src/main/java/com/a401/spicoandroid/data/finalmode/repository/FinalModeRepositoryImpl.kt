package com.a401.spicoandroid.data.finalmode.repository

import android.util.Log
import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.finalmode.api.FinalModeApi
import com.a401.spicoandroid.data.finalmode.dto.*
import com.a401.spicoandroid.domain.finalmode.model.*
import com.a401.spicoandroid.domain.finalmode.repository.FinalModeRepository
import javax.inject.Inject

class FinalModeRepositoryImpl @Inject constructor(
    private val api: FinalModeApi
) : FinalModeRepository {
    override suspend fun generateQuestions(
        projectId: Int,
        practiceId: Int,
        speechContent: String
    ): DataResource<List<FinalQuestion>> = safeApiCall {
        val request = SpeechContentRequest(speechContent)
        api.postFinalQuestions(projectId, practiceId, request)
            .getOrThrow { it.questions.map { q -> q.toDomain() } }
    }

    override suspend fun finishFinalPractice(
        projectId: Int,
        practiceId: Int,
        request: FinalModeResultRequestDto
    ): DataResource<FinalModeResult> = safeApiCall {
        val rawResponse = api.finishFinalPractice(projectId, practiceId, request)

        Log.d("FinalFlow", "응답 전체: $rawResponse")

        rawResponse.getOrThrow { it.toDomain() }
    }

}
