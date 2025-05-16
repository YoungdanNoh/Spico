package com.a401.spicoandroid.domain.report.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.report.repository.RamdomReportRepository
import javax.inject.Inject

class DeleteRandomSpeechUseCase @Inject constructor(
    private val repository: RamdomReportRepository
) {
    suspend operator fun invoke(id: Int): DataResource<Unit> {
        return repository.deleteRandomSpeech(id)
    }
}
