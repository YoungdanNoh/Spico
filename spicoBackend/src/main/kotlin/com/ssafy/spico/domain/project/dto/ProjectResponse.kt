package com.ssafy.spico.domain.project.dto

import com.ssafy.spico.domain.project.model.Project

data class ProjectResponse(
    val projectId: Int,
    val projectName: String,
    val projectDate: String
)

fun Project.toResponse(): ProjectResponse {
    this.id ?: throw IllegalArgumentException("Project id cannot be null")
    return ProjectResponse(
        projectId = this.id,
        projectName = this.title,
        projectDate = this.date.toString()
    )
}