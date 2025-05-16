package com.a401.spicoandroid.data.home.dto

import com.a401.spicoandroid.domain.home.model.HomeReport
import com.a401.spicoandroid.domain.home.model.PracticeType
import kotlinx.serialization.Serializable

@Serializable
data class HomeReportResponseDto(
    val data: ReportData?
)

@Serializable
data class ReportData(
    val reports: List<ReportItem>
)

@Serializable
data class ReportItem(
    val type: String,
    val practiceId: Int,
    val projectName: String,
    val practiceName: String,
    val reportsId: Int
) {
    fun toDomain(): HomeReport {
        return HomeReport(
            type = PracticeType.valueOf(type),
            practiceId = practiceId,
            projectName = projectName,
            practiceName = practiceName,
            reportId = reportsId // ⚠️ 'reportsId' → 'reportId'로 고쳐도 되지만 서버 응답 그대로면 유지
        )
    }
}
