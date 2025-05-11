package com.a401.spicoandroid.presentation.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Profile : NavRoutes("profile")

    // 발표 목록
    object ProjectCreate: NavRoutes("project_create")
    object ProjectScriptInput : NavRoutes("project_script_input")
    object ProjectList: NavRoutes("project_list")
    object ProjectDetail: NavRoutes("project_detail/{projectId}") {
        fun withId(projectId: Int) = "project_detail/$projectId"
    }

    // 연습 하기
    object ProjectSelect : NavRoutes("project_select/{mode}") {
        fun withMode(mode: String) = "project_select/$mode"
    }
    object FinalSetting : NavRoutes("final_setting")
    object FinalScreenCheck : NavRoutes("final_check")
    // 영상 다시 보기
    object VideoReplay : NavRoutes("video_replay")

    // 랜덤 스피치
    object RandomSpeechLanding : NavRoutes("randomspeech_landing")
    object RandomSpeechTopicSelect : NavRoutes("randomspeech_topic_select")
    object RandomSpeechSetting : NavRoutes("randomspeech_setting")
    object RandomSpeechReady : NavRoutes("randomspeech_ready")
    object RandomSpeech : NavRoutes("randomspeech")
    object RandomSpeechProjectList : NavRoutes("randomspeech_project_list")
    object RandomSpeechReport : NavRoutes("randomspeech_report/{randomSpeechId}") {
        fun withId(randomSpeechId: Int) = "randomspeech_report/$randomSpeechId"
    }

    // 코칭 모드
    object CoachingReport : NavRoutes("coaching_report")

    // 로그인
    object Login : NavRoutes("login")

}