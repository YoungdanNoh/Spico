package com.a401.spicoandroid.data.project.dto

data class ProjectUpdateRequestDto(
    val projectName: String? = null,
    val projectDate: String? = null,
    val projectTime: Int? = null,
    val script: String? = null
)
