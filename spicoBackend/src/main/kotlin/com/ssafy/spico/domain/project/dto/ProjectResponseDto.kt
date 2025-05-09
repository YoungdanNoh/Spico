package com.ssafy.spico.domain.project.dto

import com.ssafy.spico.domain.project.model.Project

data class ProjectResponseDto(
    val projectId: Int,
    val projectName: String,
    val projectDate: String
)

fun Project.toResponse(): ProjectResponseDto {
    this.id ?: throw IllegalArgumentException("Project id cannot be null")
    return ProjectResponseDto(
        projectId = this.id,
        projectName = this.title,
        projectDate = this.date.toString()
    )
}