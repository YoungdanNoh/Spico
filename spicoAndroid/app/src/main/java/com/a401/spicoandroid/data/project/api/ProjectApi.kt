package com.a401.spicoandroid.data.project.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.project.dto.ProjectDetailDto
import com.a401.spicoandroid.data.project.dto.ProjectDto
import retrofit2.http.DELETE
import retrofit2.http.GET
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
}