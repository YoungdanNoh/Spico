package com.a401.spicoandroid.presentation.practice.dummy

data class PracticeProject(
    val projectId: Int,
    val projectName: String,
    val projectDate: String
)

val DummyProjectList = listOf(
    PracticeProject(101, "자율 프로젝트 레츠고", "2025-04-28 15:00"),
    PracticeProject(104, "공통 pjt 중간발표", "2025-04-29 17:00"),
    PracticeProject(99, "특화 최최종", "2025-05-03 13:00"),
    PracticeProject(108, "관통 최종 발표", "2025-05-11 11:00")
)