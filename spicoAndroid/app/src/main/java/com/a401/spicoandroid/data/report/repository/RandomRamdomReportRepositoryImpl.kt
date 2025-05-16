package com.a401.spicoandroid.data.report.repository

import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.report.api.RandomReportApi
import com.a401.spicoandroid.domain.report.model.RandomSpeechReport
import com.a401.spicoandroid.domain.report.repository.RamdomReportRepository
import javax.inject.Inject

class RandomRamdomReportRepositoryImpl @Inject constructor(
    private val randomReportApi: RandomReportApi
) : RamdomReportRepository {

    override suspend fun getRandomSpeechReport(randomSpeechId: Int): DataResource<RandomSpeechReport> = safeApiCall {
        randomReportApi.getRandomSpeechReport(randomSpeechId).getOrThrow { it.toDomain() }
    }

    override suspend fun deleteRandomSpeech(id: Int): DataResource<Unit> = safeApiCall {
        randomReportApi.deleteRandomSpeech(id)
        Unit
    }
}
