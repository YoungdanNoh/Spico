package com.a401.spicoandroid.presentation.navigation

sealed class NavRoutes(val route: String) {
    object Main : NavRoutes("main")
    object ProjectList : NavRoutes("project_list")
    object ProjectDetail : NavRoutes("project_detail")
    object Profile : NavRoutes("profile")
}