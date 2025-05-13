package com.a401.spicoandroid.data.project.dto

import com.a401.spicoandroid.domain.project.model.ProjectDetail

data class ProjectDetailDto(
    val projectName: String,
    val projectDate: String,
    val projectTime: Int,
    val script: String
)



fun ProjectDetailDto.toDomain(): ProjectDetail {
    return ProjectDetail(
        name = projectName,
        date = projectDate,
        time = projectTime,
        script = script,
    )
}