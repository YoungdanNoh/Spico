package com.a401.spicoandroid.domain.project.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.repository.ProjectRepository
import javax.inject.Inject

class DeleteProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectId: Int): DataResource<Unit> {
        return repository.deleteProject(projectId)
    }
}