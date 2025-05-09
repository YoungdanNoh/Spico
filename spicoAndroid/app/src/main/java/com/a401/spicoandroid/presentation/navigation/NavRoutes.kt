package com.a401.spicoandroid.presentation.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")

    // 발표 목록
    object ProjectCreate: NavRoutes("project_create")
    object ProjectList: NavRoutes("project_list")
    object ProjectDetail: NavRoutes("project_detail/{projectId}") {
        fun withId(projectId: Int) = "project_detail/$projectId"
    }
    // 연습 하기
    object ProjectSelect : NavRoutes("project_select/{mode}") {
        fun withMode(mode: String) = "project_select/$mode"
    }
    object FinalSetting : NavRoutes("final_setting")
    object CoachingStart : NavRoutes("coaching_start")
}