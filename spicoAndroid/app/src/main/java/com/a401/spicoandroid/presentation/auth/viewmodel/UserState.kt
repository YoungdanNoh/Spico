package com.a401.spicoandroid.presentation.auth.viewmodel

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val openOAuthUrl: String? = null,
    val errorMessage: String? = null
)