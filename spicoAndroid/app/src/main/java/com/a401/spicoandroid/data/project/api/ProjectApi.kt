package com.a401.spicoandroid.data.project.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.project.dto.ProjectCreateRequestDto
import com.a401.spicoandroid.data.project.dto.ProjectDetailDto
import com.a401.spicoandroid.data.project.dto.ProjectDto
import com.a401.spicoandroid.data.project.dto.ProjectUpdateRequestDto
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApi {
    @GET("projects")
    suspend fun getProjectList(
        @Query("cursor") cursor: Int?,
        @Query("size") size: Int,
        @Query("type") type: String
    ): ApiResponse<List<ProjectDto>>

    @GET("projects/{projectId}")
    suspend fun getProjectDetail(
        @Path("projectId") projectId: Int
    ): ApiResponse<ProjectDetailDto>

    @DELETE("projects/{projectId}")
    suspend fun deleteProject(
        @Path("projectId") projectId: Int
    ): ApiResponse<Unit>

    @POST("projects")
    suspend fun createProject(
        @Body request: ProjectCreateRequestDto
    ): ApiResponse<Map<String, Any>>

    @PATCH("projects/{projectId}")
    suspend fun updateProject(
        @Path("projectId") projectId: Int,
        @Body request: ProjectUpdateRequestDto
    ): ApiResponse<Unit>
}