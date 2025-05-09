package com.a401.spicoandroid.data.project.repository

import com.a401.spicoandroid.data.project.api.ProjectApi
import com.a401.spicoandroid.domain.project.repository.ProjectRepository
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val api: ProjectApi
): ProjectRepository {

}