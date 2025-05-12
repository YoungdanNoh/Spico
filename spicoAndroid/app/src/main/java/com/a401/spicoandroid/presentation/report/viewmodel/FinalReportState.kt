package com.a401.spicoandroid.presentation.report.viewmodel

data class FinalReportState(
    val projectName: String = "",
    val roundCount: Int = 0,
    val modeType: String = "",
    val score: Int = 0,
    val scores: List<Float> = emptyList(),
    val qnaList: List<Pair<String, String>> = emptyList()
)
