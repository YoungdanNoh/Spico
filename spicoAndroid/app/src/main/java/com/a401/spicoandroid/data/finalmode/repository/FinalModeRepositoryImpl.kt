package com.a401.spicoandroid.data.finalmode.repository

import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.finalmode.api.FinalModeApi
import com.a401.spicoandroid.data.finalmode.dto.SpeechContentRequest
import com.a401.spicoandroid.data.finalmode.dto.toDomain
import com.a401.spicoandroid.domain.finalmode.model.FinalQuestion
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
}
