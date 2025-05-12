package com.a401.spicoandroid.presentation.project.viewmodel

import java.time.LocalDate

data class ProjectFormState(
    val projectName: String = "",
    val projectDate: LocalDate? = null,
    val projectTime: Int = 0, // 단위: 초
    val script: String = ""
)