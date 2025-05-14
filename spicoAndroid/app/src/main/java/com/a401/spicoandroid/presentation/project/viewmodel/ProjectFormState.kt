package com.a401.spicoandroid.presentation.project.viewmodel

import com.a401.spicoandroid.common.presentation.BaseState
import java.time.LocalDate

data class ProjectFormState(
    val projectName: String = "",
    val projectDate: LocalDate? = null,
    val projectTime: Int = 0, // 단위: 초
    val script: String = "",

    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    override val toastMessage: String? = null,
): BaseState