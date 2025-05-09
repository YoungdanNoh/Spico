package com.a401.spicoandroid.data.project.api

import com.a401.spicoandroid.common.data.dto.ApiResponse
import com.a401.spicoandroid.data.project.dto.ProjectListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProjectApi {
    @GET("projects")
    suspend fun getProjectList(
        @Query("cursor") cursor: Int?,
        @Query("size") size: Int,
        @Query("type") type: String
    ): ApiResponse<ProjectListResponse>
}