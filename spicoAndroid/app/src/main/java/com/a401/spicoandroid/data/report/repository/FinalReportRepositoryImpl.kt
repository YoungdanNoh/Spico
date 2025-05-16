package com.a401.spicoandroid.data.report.repository

import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.report.api.FinalReportApi
import com.a401.spicoandroid.data.report.dto.toDomain
import com.a401.spicoandroid.domain.report.model.FinalReport
import com.a401.spicoandroid.domain.report.repository.FinalReportRepository
import javax.inject.Inject

class FinalReportRepositoryImpl @Inject constructor(
    private val api: FinalReportApi
) : FinalReportRepository {
    override suspend fun getFinalReport(projectId: Int, practiceId: Int): DataResource<FinalReport> =
        safeApiCall {
            api.getFinalReport(projectId, practiceId).getOrThrow { it.toDomain() }
        }
}
