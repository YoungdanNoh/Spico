package com.a401.spicoandroid.presentation.navigation

import android.net.Uri

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
    object ProjectScriptDetail : NavRoutes("script_detail")
    object ProjectScriptEdit : NavRoutes("script_edit")
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
    object RandomSpeechSetting : NavRoutes("random_speech_setting/{topic}") {
        fun withTopic(topic: String) = "random_speech_setting/$topic"
    }
    object RandomSpeechReady : NavRoutes("random_ready?prepMin={prepMin}&speakMin={speakMin}") {
        fun withTimes(prepMin: Int, speakMin: Int) =
            "random_ready?prepMin=$prepMin&speakMin=$speakMin"
    }
    object RandomSpeech : NavRoutes("random_speech") {
        fun withQuestionAndTime(question: String, speakMin: Int): String {
            return "random_speech?question=${Uri.encode(question)}&speakMin=$speakMin"
        }
    }
    object RandomSpeechProjectList : NavRoutes("randomspeech_project_list")
    object RandomSpeechReport : NavRoutes("randomspeech_report/{randomSpeechId}") {
        fun withId(randomSpeechId: Int) = "randomspeech_report/$randomSpeechId"
    }

    // 코칭 모드
    object CoachingReport : NavRoutes("coaching_report")

    // 파이널 모드
    object FinalModeVoice : NavRoutes("final_mode_voice")
    object FinalModeAudience : NavRoutes("final_mode_audience")
    object FinalModeLoading : NavRoutes("final_mode_loading")
    object FinalReportLoading : NavRoutes("final_report_loading")
    object FinalModeQnA : NavRoutes("final_mode_qna?question={question}") {
        fun withQuestion(question: String) =
            "final_mode_qna?question=${Uri.encode(question)}"
    }
    object FinalModeReport : NavRoutes("final_mode_report")



    // 로그인
    object Login : NavRoutes("login")

}