package com.a401.spicoandroid.common.presentation

interface BaseState {
    val isLoading: Boolean
    val error: Throwable?
    val toastMessage: String?
}