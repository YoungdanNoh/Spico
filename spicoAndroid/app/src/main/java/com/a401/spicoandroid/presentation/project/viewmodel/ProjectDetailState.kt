package com.a401.spicoandroid.presentation.project.viewmodel

import com.a401.spicoandroid.common.presentation.BaseState
import com.a401.spicoandroid.domain.project.model.ProjectDetail

data class ProjectDetailState(
    val id: Int = 0,
    val project: ProjectDetail? = null,
    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    override val toastMessage: String? = null,
): BaseState