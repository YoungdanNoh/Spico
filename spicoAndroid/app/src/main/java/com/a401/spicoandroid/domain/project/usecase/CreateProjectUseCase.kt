package com.a401.spicoandroid.domain.project.usecase

import com.a401.spicoandroid.common.domain.DataResource
import com.a401.spicoandroid.domain.project.repository.ProjectRepository
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(
        name: String,
        date: String,
        time: Int,
        script: String
    ): DataResource<Int> {
        return repository.createProject(name, date, time, script)
    }
}
