package com.a401.spicoandroid.presentation.navigation

sealed class NavRoutes(val route: String) {
    object Main : NavRoutes("main")
}