package com.a401.spicoandroid.presentation.navigation

import android.net.Uri
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeLoadingType

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
    object ModeSelect : NavRoutes("mode_select")
    object ProjectSelect : NavRoutes("project_select")
    object FinalSetting : NavRoutes("final_setting")
    object FinalScreenCheck : NavRoutes("final_check")

    // 랜덤 스피치
    object RandomSpeechLanding : NavRoutes("randomspeech_landing")
    object RandomSpeechTopicSelect : NavRoutes("randomspeech_topic_select")
    object RandomSpeechSetting : NavRoutes("randomspeech_setting")
    object RandomSpeechReady : NavRoutes("randomspeech_ready")
    object RandomSpeech : NavRoutes("randomspeech")
    object RandomSpeechList : NavRoutes("randomspeech_list")
    object RandomSpeechReport : NavRoutes("randomspeech_report/{randomSpeechId}") {
        fun withId(randomSpeechId: Int) = "randomspeech_report/$randomSpeechId"
    }

    // 코칭 모드
    object CoachingMode : NavRoutes("coaching_mode")
    object CoachingReport : NavRoutes("coaching_report")

    // 파이널 모드
    object FinalModeAudience : NavRoutes("final_mode_audience/{projectId}/{practiceId}") {
        fun withArgs(projectId: Int, practiceId: Int) = "final_mode_audience/$projectId/$practiceId"
    }

    object FinalModeVoice : NavRoutes("final_mode_voice/{projectId}/{practiceId}") {
        fun withArgs(projectId: Int, practiceId: Int) = "final_mode_voice/$projectId/$practiceId"
    }

    object FinalModeQnA : NavRoutes("final_mode_qna/{projectId}/{practiceId}") {
        fun withArgs(projectId: Int, practiceId: Int) = "final_mode_qna/$projectId/$practiceId"
    }

    object FinalModeLoading : NavRoutes("final_mode_loading/{type}/{projectId}/{practiceId}") {
        fun withArgs(type: FinalModeLoadingType, projectId: Int, practiceId: Int) =
            "final_mode_loading/${type.name}/$projectId/$practiceId"
    }

    object FinalReport : NavRoutes("final_mode_report/{projectId}/{practiceId}") {
        fun createRoute(projectId: Int, practiceId: Int): String =
            "final_mode_report/$projectId/$practiceId"
    }

    // 영상 다시 보기
    object VoiceScript : NavRoutes("voice_script/{projectId}/{practiceId}") {
        fun withArgs(projectId: Int, practiceId: Int) = "voice_script/$projectId/$practiceId"
    }

    object VideoReplay : NavRoutes("video_replay/{encodedUrl}") {
        fun withEncodedUrl(presignedUrl: String): String =
            "video_replay/${Uri.encode(presignedUrl)}"
    }

    // 로그인
    object Login : NavRoutes("login")

    //에러
    object NotFound : NavRoutes("not_found")

    // stt 테스트
    object SpeechTest : NavRoutes("speech_test")

}