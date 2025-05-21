package com.a401.spicoandroid.domain.project.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.model.ProjectDetail
import com.a401.spicoandroid.domain.project.repository.ProjectRepository
import javax.inject.Inject

class GetProjectDetailUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(
        projectId: Int
    ): DataResource<ProjectDetail> {
        return repository.getProjectDetail(projectId)
    }
}