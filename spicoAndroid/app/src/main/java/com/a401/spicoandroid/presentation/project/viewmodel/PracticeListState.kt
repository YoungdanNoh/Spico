package com.a401.spicoandroid.presentation.project.viewmodel

import com.a401.spicoandroid.common.presentation.BaseState
import com.a401.spicoandroid.domain.practice.model.Practice

data class PracticeListState(
    val practices: List<Practice> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    override val toastMessage: String? = null,
) : BaseState