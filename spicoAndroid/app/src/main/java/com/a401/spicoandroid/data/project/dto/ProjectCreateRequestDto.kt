package com.a401.spicoandroid.data.project.dto

data class ProjectCreateRequestDto (
    val projectName: String,
    val projectDate: String, // ISO 형식 (ex: "2025-05-13")
    val projectTime: Int,    // 초 단위
    val script: String
)