package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.a401.spicoandroid.presentation.auth.screen.LoginScreen
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeAudienceScreen
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeLoadingScreen
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeLoadingType
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeQnAScreen
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeVoiceScreen
import com.a401.spicoandroid.presentation.home.screen.HomeScreen
import com.a401.spicoandroid.presentation.home.viewmodel.WeeklyCalendarViewModel
import com.a401.spicoandroid.presentation.mypage.screen.MyPageScreen
import com.a401.spicoandroid.presentation.practice.screen.FinalSettingScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectDetailScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectListScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectScriptInputScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectSettingScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectScriptDetailScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectScriptEditScreen
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectViewModel
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechLandingScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechProjectListScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechReadyScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechSettingScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechTopicSelectScreen
import com.a401.spicoandroid.presentation.report.screen.VideoReplayScreen
import com.a401.spicoandroid.presentation.report.screen.CoachingReportScreen
import com.a401.spicoandroid.presentation.report.screen.FinalReportScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavControllerProvider(navController = navController) {
        val weeklyCalendarViewModel: WeeklyCalendarViewModel = hiltViewModel()
        val projectViewModel: ProjectViewModel = hiltViewModel()

        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route,
            modifier = modifier
        ) {
            composable(NavRoutes.Home.route) {
                HomeScreen(navController, modifier, weeklyCalendarViewModel)
            }

            // 마이페이지
            composable(NavRoutes.Profile.route){
                MyPageScreen(navController, modifier)
            }

            composable(NavRoutes.ProjectCreate.route) {
                ProjectSettingScreen(navController, modifier)
            }

            composable(NavRoutes.ProjectScriptInput.route) {
                ProjectScriptInputScreen(navController)
            }

            composable(NavRoutes.ProjectList.route) {
                ProjectListScreen(navController, projectViewModel, {})
            }

            composable(NavRoutes.ProjectScriptDetail.route) {
                ProjectScriptDetailScreen(
                    navController,
                    onEdit = {
                        navController.navigate(NavRoutes.ProjectScriptEdit.route)
                    }
                )
            }

            composable(NavRoutes.ProjectScriptEdit.route) {
                ProjectScriptEditScreen(
                    navController,
                    onComplete = {
                        navController.popBackStack(NavRoutes.ProjectScriptDetail.route, inclusive = false)
                    }
                )
            }


            composable(NavRoutes.VideoReplay.route) {
                VideoReplayScreen()
            }

            composable(
                route = NavRoutes.ProjectDetail.route,
                arguments = listOf(navArgument("projectId") { type = NavType.IntType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: -1
                ProjectDetailScreen(projectId = projectId)
            }
            // 연습하기
            composable(
                route = NavRoutes.ProjectSelect.route,
                arguments = listOf(navArgument("mode") { type = NavType.StringType })
            ) { backStackEntry ->
                val mode = backStackEntry.arguments?.getString("mode") ?: "coaching"
                com.a401.spicoandroid.presentation.practice.screen.ProjectSelectScreen(
                    navController = navController,
                    mode = mode
                )
            }
            // 파이널 모드 설정 화면
            composable(NavRoutes.FinalSetting.route) {
                FinalSettingScreen(navController = navController)
            }
            composable(NavRoutes.FinalScreenCheck.route) {
                com.a401.spicoandroid.presentation.practice.screen.FinalScreenCheckScreen(
                    navController = navController
                )
            }
            // 랜덤 스피치
            composable(NavRoutes.RandomSpeechLanding.route) {
                RandomSpeechLandingScreen()
            }
            composable(NavRoutes.RandomSpeechTopicSelect.route) {
                RandomSpeechTopicSelectScreen(navController = navController)
            }
            composable(
                route = NavRoutes.RandomSpeechSetting.route,
                arguments = listOf(navArgument("topic") { type = NavType.StringType })
            ) { backStackEntry ->
                val topic = backStackEntry.arguments?.getString("topic") ?: "unknown"
                RandomSpeechSettingScreen(
                    topic = topic,
                    navController = navController,
                    onNext = { prepMin, speakMin ->
                        navController.navigate(
                            NavRoutes.RandomSpeechReady.withTimes(prepMin, speakMin)
                        )
                    }
                )
            }
            composable(
                route = NavRoutes.RandomSpeechReady.route,
                arguments = listOf(
                    navArgument("prepMin") { type = NavType.IntType },
                    navArgument("speakMin") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val prepMin = backStackEntry.arguments?.getInt("prepMin") ?: 1
                val speakMin = backStackEntry.arguments?.getInt("speakMin") ?: 3

                RandomSpeechReadyScreen(
                    prepMin = prepMin,
                    speakMin = speakMin,
                    onEndClick = {
                        navController.navigate(NavRoutes.RandomSpeechLanding.route) {
                            popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = true }
                        }
                    },
                    onStartClick = { question, speakMinValue ->
                        navController.navigate(
                            NavRoutes.RandomSpeech.withQuestionAndTime(question, speakMinValue)
                        )
                    }
                )
            }
            composable(
                route = "random_speech?question={question}&speakMin={speakMin}",
                arguments = listOf(
                    navArgument("question") { type = NavType.StringType },
                    navArgument("speakMin") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val question = backStackEntry.arguments?.getString("question") ?: ""
                val speakMin = backStackEntry.arguments?.getInt("speakMin") ?: 3

                RandomSpeechScreen(
                    question = question,
                    speakMin = speakMin,
                    onFinish = {
                        navController.navigate(NavRoutes.RandomSpeechLanding.route) {
                            popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavRoutes.RandomSpeechProjectList.route) {
                RandomSpeechProjectListScreen(
                    onProjectClick = { id ->
                        navController.navigate(NavRoutes.RandomSpeechReport.withId(id))
                    },
                    onStartClick = {
                        navController.navigate(NavRoutes.RandomSpeechTopicSelect.route)
                    }
                )
            }
            // 랜덤 스피치 리포트
            composable(
                route = NavRoutes.RandomSpeechReport.route,
                arguments = listOf(navArgument("randomSpeechId") { type = NavType.IntType })
            ) { backStackEntry ->
                val randomSpeechId = backStackEntry.arguments?.getInt("randomSpeechId") ?: -1
                com.a401.spicoandroid.presentation.report.screen.RandomSpeechReportScreen(
                    navController = navController,
                    randomSpeechId = randomSpeechId)
            }

            // 코칭 모드
            composable(NavRoutes.CoachingReport.route) {
                CoachingReportScreen(navController)
            }

            // 파이널 모드
            composable("final_mode_voice") {
                FinalModeVoiceScreen(navController)
            }
            composable("final_mode_audience") {
                FinalModeAudienceScreen(navController)
            }
            composable("final_mode_loading") {
                FinalModeLoadingScreen(
                    navController = navController,
                    type = FinalModeLoadingType.QUESTION
                )
            }
            composable("final_report_loading") {
                FinalModeLoadingScreen(
                    navController = navController,
                    type = FinalModeLoadingType.REPORT
                )
            }
            composable(
                route = NavRoutes.FinalModeQnA.route,
                arguments = listOf(navArgument("question") { defaultValue = "기본 질문입니다." })
            ) { backStackEntry ->
                val question = backStackEntry.arguments?.getString("question") ?: ""
                FinalModeQnAScreen(
                    navController = navController,
                    question = question)
            }
            composable("final_mode_report") {
                FinalReportScreen()
            }

            // 로그인
            composable(NavRoutes.Login.route) {
                LoginScreen(onKakaoLoginClick = {})
            }
        }
    }
}