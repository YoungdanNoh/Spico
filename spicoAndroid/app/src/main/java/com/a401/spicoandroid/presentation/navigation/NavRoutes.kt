package com.a401.spicoandroid.presentation.navigation

sealed class NavRoutes(val route: String) {
    object Main : NavRoutes("main")

    object ProjectCreate: NavRoutes("project_create")
    object ProjectList: NavRoutes("project_list")
    object ProjectDetail: NavRoutes("project_detail/{projectId}") {
        fun withId(projectId: Int) = "project_detail/$projectId"
    }
}