package com.ssafy.spico.domain.project.service

import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.dto.UpdateProjectRequestDto
import com.ssafy.spico.domain.project.model.Project

interface ProjectService {
    fun getProjects(userId: Int, cursor: Int?, size: Int, type: ProjectViewType): List<Project>
    fun createProject(project: Project): Int
    fun updateProject(projectId: Int, request: UpdateProjectRequestDto)
    fun deleteProject(projectId: Int)
    fun getProjectDetail(projectId: Int): Project
}