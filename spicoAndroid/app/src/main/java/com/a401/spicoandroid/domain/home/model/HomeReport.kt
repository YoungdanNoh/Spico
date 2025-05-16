package com.a401.spicoandroid.domain.home.model

enum class PracticeType {
    COACHING, FINAL
}

data class HomeReport(
    val type: PracticeType,
    val projectId: Int,
    val practiceId: Int,
    val projectName: String,
    val practiceName: String,
    val reportId: Int
)