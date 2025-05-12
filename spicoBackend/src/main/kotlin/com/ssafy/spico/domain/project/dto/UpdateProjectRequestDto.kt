package com.ssafy.spico.domain.project.dto

import com.ssafy.spico.domain.project.exception.ProjectError
import com.ssafy.spico.domain.project.exception.ProjectException
import com.ssafy.spico.domain.project.model.UpdateProjectCommand
import java.time.LocalDate

data class UpdateProjectRequestDto (
    val projectName: String?,
    val projectDate: String?,
    val projectTime: Int?,
    val script: String?
)

fun UpdateProjectRequestDto.toCommand(): UpdateProjectCommand {
    return UpdateProjectCommand(
        title = this.projectName,
        date = this.projectDate?.let {
            try {
                LocalDate.parse(it)
            } catch (e: Exception) {
                throw ProjectException(ProjectError.INVALID_DATE_FORMAT)
            }
        },
        limitTime = this.projectTime,
        script = this.script
    )
}