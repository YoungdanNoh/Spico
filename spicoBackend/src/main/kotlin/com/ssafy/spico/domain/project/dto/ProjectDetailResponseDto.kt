package com.ssafy.spico.domain.project.dto

import com.ssafy.spico.domain.project.model.Project

data class ProjectDetailResponseDto(
    val projectName: String,
    val projectDate: String,
    val projectTime: Int,
    val script: String
)

fun Project.toDetailResponse(): ProjectDetailResponseDto {
    return ProjectDetailResponseDto(
        projectName = this.title,
        projectDate = this.date.toString(),
        projectTime = this.limitTime,
        script = this.script?: ""
    )
}