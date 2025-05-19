package com.ssafy.spico.domain.project.service

import com.ssafy.spico.domain.practice.dto.PracticeListResponseDto
import com.ssafy.spico.domain.practice.dto.PracticeResponseDto
import com.ssafy.spico.domain.practice.entity.PracticeType
import com.ssafy.spico.domain.practice.model.Practice
import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.dto.UpdateProjectRequestDto
import com.ssafy.spico.domain.project.model.Project

interface ProjectService {
    fun getProjects(userId: Int, cursor: Int?, size: Int, type: ProjectViewType): List<Project>
    fun createProject(project: Project): Int
    fun updateProject(projectId: Int, request: UpdateProjectRequestDto)
    fun deleteProject(userId: Int, projectId: Int)
    fun getProjectDetail(projectId: Int): Project
    fun getPractices(userId: Int, projectId: Int, practiceFilter: PracticeType?, cursor: Int?, size: Int): List<PracticeResponseDto>
}