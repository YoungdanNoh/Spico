package com.a401.spicoandroid.data.project.dto

import com.a401.spicoandroid.data.practice.dto.PracticeDto
import com.a401.spicoandroid.domain.project.model.Project
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ProjectDto(
    val projectId: Int,
    val projectName: String,
    val projectDate: String
)

data class PracticeListResponseDto(
    val practices: List<PracticeDto>
)

fun ProjectDto.toDomain(): Project {
    return Project(
        id = this.projectId,
        title = this.projectName,
        date = LocalDate.parse(this.projectDate, DateTimeFormatter.ISO_DATE)
    )
}