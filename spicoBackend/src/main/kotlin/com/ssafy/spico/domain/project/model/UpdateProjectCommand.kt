package com.ssafy.spico.domain.project.model

import java.time.LocalDate

data class UpdateProjectCommand(
    val title: String?,
    val date: LocalDate?,
    val limitTime: Int?,
    val script: String?
)