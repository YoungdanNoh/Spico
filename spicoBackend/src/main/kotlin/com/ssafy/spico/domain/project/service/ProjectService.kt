package com.ssafy.spico.domain.project.service

import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.model.Project

interface ProjectService {
    fun getProjects(cursor: Int?, size: Int, type: ProjectViewType): List<Project>
    fun createProject(project: Project): Int
}