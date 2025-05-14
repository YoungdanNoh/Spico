package com.a401.spicoandroid.presentation.project.viewmodel

data class ProjectDeleteState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)