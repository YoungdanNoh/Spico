package com.a401.spicoandroid.domain.project.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.repository.ProjectRepository
import javax.inject.Inject

class UpdateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        name: String? = null,
        date: String? = null,
        time: Int? = null,
        script: String? = null
    ): DataResource<Unit> {
        return repository.updateProject(projectId, name, date, time, script)
    }
}
