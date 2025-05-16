package com.a401.spicoandroid.domain.report.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.report.model.RandomSpeechReport
import com.a401.spicoandroid.domain.report.repository.RamdomReportRepository
import javax.inject.Inject

class GetRandomSpeechReportUseCase @Inject constructor(
    private val repository: RamdomReportRepository
) {
    suspend operator fun invoke(randomSpeechId: Int): DataResource<RandomSpeechReport> {
        return repository.getRandomSpeechReport(randomSpeechId)
    }
}
