package com.ssafy.spico.domain.project.model

import com.ssafy.spico.domain.user.entity.UserEntity
import com.ssafy.spico.domain.project.entity.ProjectEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Project(
    val id: Int?,
    val userId: Int,
    val title: String,
    val date: LocalDate,
    val limitTime: LocalTime,
    val script: String?,
    val createdAt: LocalDateTime
)

fun Project.toEntity(userEntity: UserEntity): ProjectEntity {
    return ProjectEntity(
        userEntity,
        this.title,
        this.date,
        this.limitTime,
        this.script,
        this.createdAt
    )
}

fun ProjectEntity.toModel(): Project {
    return Project(
        id = this.projectId,
        userId = this.userEntity.id,
        title = this.title,
        date = this.date,
        limitTime = this.limitTime,
        script = this.script,
        createdAt = this.createdAt
    )
}