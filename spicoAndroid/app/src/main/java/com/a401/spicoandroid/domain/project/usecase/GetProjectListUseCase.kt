package com.a401.spicoandroid.domain.project.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.model.Project
import com.a401.spicoandroid.domain.project.model.ProjectScreenType
import com.a401.spicoandroid.domain.project.repository.ProjectRepository
import javax.inject.Inject

class GetProjectListUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(
        cursor: Int?,
        size: Int,
        screenType: ProjectScreenType
    ): DataResource<List<Project>> {
        val projects: DataResource<List<Project>> = repository.getProjectList(cursor, size, screenType)
        return projects
    }
}