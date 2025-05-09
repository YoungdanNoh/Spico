package com.ssafy.spico.domain.project.controller

import com.ssafy.spico.common.response.ApiResponse
import com.ssafy.spico.domain.project.dto.ProjectResponse
import com.ssafy.spico.domain.project.dto.ProjectViewType
import com.ssafy.spico.domain.project.dto.toResponse
import com.ssafy.spico.domain.project.service.ProjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController(
    private val projectService: ProjectService
) {
    @GetMapping
    fun getProjects(
        @RequestParam cursor: Int?,
        @RequestParam size: Int,
        @RequestParam type: ProjectViewType,
    ): ApiResponse<List<ProjectResponse>> {
        val projects = projectService.getProjects(cursor, size, type)
        val response = projects.map { it.toResponse() }
        return ApiResponse.success(response)
    }
}