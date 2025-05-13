package com.a401.spicoandroid.presentation.report.viewmodel
import com.a401.spicoandroid.ui.component.TimeSegment


data class FinalReportState(
    val projectName: String = "",
    val roundCount: Int = 0,
    val modeType: String = "",
    val score: Int = 0,
    val scores: List<Float> = emptyList(),
    val qnaList: List<Pair<String, String>> = emptyList(),
    val reportItems: List<ReportCategoryData> = emptyList()
)

data class ReportCategoryData(
    val title: String,
    val description: String,
    val iconResId: Int,
    val timeRangeText: String,
    val segments: List<TimeSegment>,
    val totalStartMillis: Long = 0L,
    val totalEndMillis: Long = 180000L,
    val progress: Float? = null
)

