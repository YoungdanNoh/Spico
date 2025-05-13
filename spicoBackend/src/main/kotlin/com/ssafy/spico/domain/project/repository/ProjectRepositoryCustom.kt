package com.ssafy.spico.domain.project.repository

import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.entity.ProjectEntity

interface ProjectRepositoryCustom {
    fun findProjectsWithPaging(
        userId: Int,
        cursor: Int?,
        size: Int,
        type: ProjectViewType
    ): List<ProjectEntity>
}