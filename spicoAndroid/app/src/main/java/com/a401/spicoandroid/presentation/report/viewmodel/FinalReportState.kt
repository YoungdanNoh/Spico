package com.a401.spicoandroid.presentation.report.viewmodel

import com.a401.spicoandroid.R
import com.a401.spicoandroid.domain.report.model.FinalReport
import com.a401.spicoandroid.ui.component.TimeSegment
import java.time.LocalDateTime
import java.time.ZoneId

data class FinalReportState(
    val projectName: String = "",
    val roundCount: Int = 0,
    val modeType: String = "",
    val score: Int = 0,
    val scores: List<Float> = emptyList(),
    val qnaList: List<Pair<String, String>> = emptyList(),
    val reportItems: List<ReportCategoryData> = emptyList(),
    val videoUrl: String? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val voiceScript: String? = null
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

fun FinalReport.toUiState(): FinalReportState {
    val volumeSegments = volumeRecords.map {
        TimeSegment(it.startTime.toMillis(), it.endTime.toMillis())
    }

    val speedSegments = speedRecords.map {
        TimeSegment(it.startTime.toMillis(), it.endTime.toMillis())
    }

    val pauseSegments = pauseRecords.map {
        TimeSegment(it.startTime.toMillis(), it.endTime.toMillis())
    }

    val reportItems = listOf(
        ReportCategoryData(
            title = "발음",
            description = "특정 구간에서 발음이 뭉개졌어요.",
            iconResId = R.drawable.img_feedback_pronunciation,
            timeRangeText = "전체 분석 기반",
            segments = emptyList(),
            progress = pronunciationScore / 10f
        ),
        ReportCategoryData(
            title = "속도",
            description = when (speedStatus) {
                "FAST" -> "일부 구간에서 너무 빠르게 말했어요."
                "SLOW" -> "일부 구간에서 너무 느리게 말했어요."
                else -> "적절한 속도로 말했어요."
            },
            iconResId = R.drawable.img_feedback_speed,
            timeRangeText = speedSegments.toRangeText(),
            segments = speedSegments,
            progress = speedScore / 10f
        ),
        ReportCategoryData(
            title = "성량",
            description = when (volumeStatus) {
                "LOUD" -> "일부 구간에서 목소리가 너무 컸어요."
                "QUIET" -> "일부 구간에서 목소리가 너무 작았어요."
                else -> "적절한 성량으로 말했어요."
            },
            iconResId = R.drawable.img_feedback_volume,
            timeRangeText = volumeSegments.toRangeText(),
            segments = volumeSegments,
            progress = volumeScore / 10f
        ),
        ReportCategoryData(
            title = "휴지",
            description = "발표 중 ${pauseCount}번의 멈춤이 있었어요.",
            iconResId = R.drawable.img_feedback_silence,
            timeRangeText = pauseSegments.toRangeText(),
            segments = pauseSegments,
            progress = pauseScore / 10f
        ),
        ReportCategoryData(
            title = "대본일치도",
            description = "스크립트와의 일치율은 $scriptMatchRate%입니다.",
            iconResId = R.drawable.img_feedback_script_match,
            timeRangeText = "전체 분석 기반",
            segments = emptyList(),
            progress = scriptMatchRate / 100f
        )
    )

    return FinalReportState(
        projectName = projectName,
        roundCount = practiceName.filter { it.isDigit() }.toIntOrNull() ?: 0,
        modeType = "파이널 모드",
        score = totalScore,
        scores = listOf(
            pronunciationScore.toFloat(),
            speedScore.toFloat(),
            volumeScore.toFloat(),
            pauseScore.toFloat(),
            scriptMatchRate.toFloat()
        ),
        qnaList = qaRecord.map { it.question to it.answer },
        reportItems = reportItems,
        videoUrl = videoUrl,
        voiceScript = voiceScript
    )
}

// 유틸 함수

fun String.toMillis(): Long {
    return LocalDateTime.parse(this)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun Long.toMinSec(): String {
    val minutes = this / 60000
    val seconds = (this % 60000) / 1000
    return "%d:%02d".format(minutes, seconds)
}

fun List<TimeSegment>.toRangeText(): String {
    if (isEmpty()) return "00:00 ~ 00:00"
    val start = this.first().startMillis
    val end = this.last().endMillis
    return "${start.toMinSec()} ~ ${end.toMinSec()}"
}
