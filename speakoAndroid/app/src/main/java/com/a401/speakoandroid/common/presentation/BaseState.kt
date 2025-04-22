package com.a401.speakoandroid.common.presentation

interface BaseState {
    val isLoading: Boolean
    val error: Throwable?
    val toastMessage: String?
}