package com.a401.spicoandroid.domain.project.model

import java.time.LocalDate

data class Project(
    val id: Int,
    val title: String,
    val date: LocalDate
)