package com.a401.spicoandroid.domain.report.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.report.model.RandomSpeechReport

interface ReportRepository {
    suspend fun getRandomSpeechReport(randomSpeechId: Int): DataResource<RandomSpeechReport>
}
