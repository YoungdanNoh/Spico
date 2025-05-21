package com.a401.spicoandroid.data.report.repository

import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.report.api.ReportApi
import com.a401.spicoandroid.domain.report.model.CoachingReport
import com.a401.spicoandroid.domain.report.model.RandomSpeechReport
import com.a401.spicoandroid.domain.report.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi
) : ReportRepository {

    override suspend fun getRandomSpeechReport(randomSpeechId: Int): DataResource<RandomSpeechReport> = safeApiCall {
        reportApi.getRandomSpeechReport(randomSpeechId).getOrThrow { it.toDomain() }
    }

    override suspend fun deleteRandomSpeech(id: Int): DataResource<Unit> = safeApiCall {
        reportApi.deleteRandomSpeech(id)
        Unit
    }

    override suspend fun getCoachingReport(projectId: Int, practiceId: Int): DataResource<CoachingReport> = safeApiCall {
        reportApi.getCoachingReport(projectId, practiceId).getOrThrow { it.toDomain() }
    }
}
