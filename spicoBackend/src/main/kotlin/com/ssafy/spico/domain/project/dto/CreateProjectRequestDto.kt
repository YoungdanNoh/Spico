package com.ssafy.spico.domain.project.dto

import com.ssafy.spico.domain.project.model.Project
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateProjectRequestDto (
    val projectName: String,
    val projectDate: String,
    val projectTime: Int,
    val script: String
)

fun CreateProjectRequestDto.toProject(userId: Int): Project {
    return Project(
        userId = userId,
        title = projectName,
        date = LocalDate.parse(projectDate),
        limitTime = projectTime,
        script = script,
        createdAt = LocalDateTime.now(),
        lastCoachingCnt = 0,
        lastFinalCnt = 0
    )
}