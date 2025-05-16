package com.a401.spicoandroid.domain.report.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.report.model.FinalReport
import com.a401.spicoandroid.domain.report.repository.FinalReportRepository
import javax.inject.Inject

class GetFinalReportUseCase @Inject constructor(
    private val repository: FinalReportRepository
) {
    suspend operator fun invoke(projectId: Int, practiceId: Int): DataResource<FinalReport> {
        return repository.getFinalReport(projectId, practiceId)
    }
}
