package com.ssafy.spico.domain.practice.model

import com.ssafy.spico.domain.practice.entity.PracticeEntity
import com.ssafy.spico.domain.practice.entity.PracticeStatus
import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.project.entity.ProjectEntity
import java.time.LocalDateTime

data class Practice(
    val id: Int? = null,
    val projectEntity: ProjectEntity,
    val createdAt: LocalDateTime,
    val type: PracticeType,
    val status: PracticeStatus
)

fun Practice.toEntity(): PracticeEntity {
    return PracticeEntity(
        this.projectEntity,
        this.createdAt,
        this.type,
        this.status
    )
}

fun PracticeEntity.toModel(): Practice {
    return Practice(
        id = this.practiceId,
        projectEntity = this.projectEntity,
        createdAt = this.createdAt,
        type = this.type,
        status = this.status
    )
}