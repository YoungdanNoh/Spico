package com.a401.spicoandroid.domain.report.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.report.model.FinalReport

interface FinalReportRepository {
    suspend fun getFinalReport(projectId: Int, practiceId: Int): DataResource<FinalReport>
}
