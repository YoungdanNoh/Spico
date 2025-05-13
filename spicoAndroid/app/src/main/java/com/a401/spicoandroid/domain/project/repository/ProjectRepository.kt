package com.a401.spicoandroid.domain.project.repository

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.model.Project
import com.a401.spicoandroid.domain.project.model.ProjectDetail
import com.a401.spicoandroid.domain.project.model.ProjectScreenType

interface ProjectRepository {
    suspend fun getProjectList(
        cursor: Int?,
        size: Int,
        screenType: ProjectScreenType
    ): DataResource<List<Project>>

    suspend fun getProjectDetail(
        projectId: Int
    ): DataResource<ProjectDetail>
}