package com.a401.spicoandroid.data.project.repository

import com.a401.spicoandroid.common.data.dto.getOrThrow
import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.common.utils.safeApiCall
import com.a401.spicoandroid.data.project.api.ProjectApi
import com.a401.spicoandroid.data.project.dto.ProjectCreateRequestDto
import com.a401.spicoandroid.data.project.dto.toDomain
import com.a401.spicoandroid.domain.project.model.Project
import com.a401.spicoandroid.domain.project.model.ProjectDetail
import com.a401.spicoandroid.domain.project.model.ProjectScreenType
import com.a401.spicoandroid.domain.project.repository.ProjectRepository
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val api: ProjectApi
): ProjectRepository {

    override suspend fun getProjectList(
        cursor: Int?,
        size: Int,
        screenType: ProjectScreenType
    ): DataResource<List<Project>> = safeApiCall {
        api.getProjectList(cursor, size, screenType.name).getOrThrow { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getProjectDetail(
        projectId: Int
    ): DataResource<ProjectDetail> = safeApiCall {
        api.getProjectDetail(projectId).getOrThrow { it.toDomain() }
    }

    override suspend fun createProject(
        name: String,
        date: String,
        time: Int,
        script: String
    ): DataResource<Int> = safeApiCall {
        val request = ProjectCreateRequestDto(
            projectName = name,
            projectDate = date,
            projectTime = time,
            script = script
        )
        api.createProject(request).getOrThrow { data ->
            data["projectId"] as? Int ?: throw IllegalStateException("projectId not found")
        }
    }

}