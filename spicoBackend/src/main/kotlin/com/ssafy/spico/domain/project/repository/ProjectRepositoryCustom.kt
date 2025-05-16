package com.ssafy.spico.domain.project.repository

import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.entity.PracticesEntity
import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.entity.ProjectEntity

interface ProjectRepositoryCustom {
    fun findProjectsWithPaging(
        userId: Int,
        cursor: Int?,
        size: Int,
        type: ProjectViewType
    ): List<ProjectEntity>

    fun findPracticesByProjectIdWithPaging(
        userId: Int,
        projectId: Int,
        filter: PracticeType?,  // COACHING, FINAL, 또는 null
        cursor: Int?,
        size: Int
    ): List<PracticesEntity>
}