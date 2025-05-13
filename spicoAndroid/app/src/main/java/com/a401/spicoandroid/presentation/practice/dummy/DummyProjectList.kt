package com.a401.spicoandroid.presentation.practice.dummy

import com.a401.spicoandroid.domain.project.model.Project
import java.time.LocalDate

val DummyProjectList = listOf(
    Project(id = 101, title = "자율 프로젝트 레츠고", date = LocalDate.parse("2025-04-28")),
    Project(id = 104, title = "공통 pjt 중간발표", date = LocalDate.parse("2025-04-29")),
    Project(id = 99, title = "특화 최최종", date = LocalDate.parse("2025-05-03")),
    Project(id = 108, title = "관통 최종 발표", date = LocalDate.parse("2025-05-11"))
)

