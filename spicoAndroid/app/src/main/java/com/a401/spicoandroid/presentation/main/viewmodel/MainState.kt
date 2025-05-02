package com.a401.spicoandroid.presentation.main.viewmodel

import com.a401.spicoandroid.common.presentation.BaseState

data class MainState(
    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    override val toastMessage: String? = null
): BaseState