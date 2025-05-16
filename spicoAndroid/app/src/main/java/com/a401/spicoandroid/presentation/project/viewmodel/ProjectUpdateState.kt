package com.a401.spicoandroid.presentation.project.viewmodel

data class ProjectUpdateState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Throwable? = null
)
