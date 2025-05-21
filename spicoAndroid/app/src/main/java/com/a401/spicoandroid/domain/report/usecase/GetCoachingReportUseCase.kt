package com.a401.spicoandroid.domain.report.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.report.model.CoachingReport
import com.a401.spicoandroid.domain.report.repository.ReportRepository
import javax.inject.Inject

class GetCoachingReportUseCase @Inject constructor(
    private val repository: ReportRepository
){
    suspend operator fun invoke(projectId: Int, practiceId: Int): DataResource<CoachingReport> {
        return repository.getCoachingReport(projectId, practiceId)
    }
}