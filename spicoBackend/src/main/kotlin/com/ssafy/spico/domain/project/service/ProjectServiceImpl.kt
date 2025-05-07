package com.ssafy.spico.domain.project.service

import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.model.Project
import com.ssafy.spico.domain.project.repository.ProjectRepository
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository
): ProjectService {
    fun createProject() {}
    override fun getProjects(cursor: Long?, size: Int, type: ProjectViewType): List<Project> {
        TODO("Not yet implemented")
    }
}