package com.a401.spicoandroid.domain.report.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.report.model.CoachingReport
import com.a401.spicoandroid.domain.report.model.RandomSpeechReport

interface ReportRepository {
    suspend fun getRandomSpeechReport(randomSpeechId: Int): DataResource<RandomSpeechReport>
    suspend fun deleteRandomSpeech(id: Int): DataResource<Unit>
    suspend fun getCoachingReport(projectId: Int, practiceId: Int): DataResource<CoachingReport>
}
