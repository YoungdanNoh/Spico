package com.ssafy.spico.domain.project.controller

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.project.dto.*
import com.ssafy.spico.domain.project.service.ProjectService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    private val projectService: ProjectService,
    @Value("\${user-id}") private val userId: Int
) {
    @GetMapping
    fun getProjects(
        @RequestParam cursor: Int?,
        @RequestParam size: Int,
        @RequestParam type: ProjectViewType,
    ): ApiResponse<List<ProjectResponseDto>> {
        val projects = projectService.getProjects(cursor, size, type)
        val response = projects.map { it.toResponse() }
        return ApiResponse.success(response)
    }

    @PostMapping
    fun createProject(
        @RequestBody request: CreateProjectRequestDto
    ): ApiResponse<CreateProjectResponseDto> {
        val project = request.toProject(userId)
        val projectId = projectService.createProject(project)
        return ApiResponse.success(CreateProjectResponseDto(projectId))
    }
}