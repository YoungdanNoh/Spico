package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.a401.spicoandroid.infrastructure.speech.SpeechTestScreen
import com.a401.spicoandroid.presentation.auth.screen.LoginScreen
import com.a401.spicoandroid.presentation.coachingmode.screen.CoachingModeScreen
import com.a401.spicoandroid.presentation.error.screen.NotFoundScreen
import com.a401.spicoandroid.presentation.auth.viewmodel.LoginViewModel
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeAudienceScreen
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeLoadingScreen
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeLoadingType
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeQnAScreen
import com.a401.spicoandroid.presentation.finalmode.screen.FinalModeVoiceScreen
import com.a401.spicoandroid.presentation.home.screen.HomeScreen
import com.a401.spicoandroid.presentation.home.viewmodel.WeeklyCalendarViewModel
import com.a401.spicoandroid.presentation.mypage.screen.MyPageScreen
import com.a401.spicoandroid.presentation.practice.screen.FinalScreenCheckScreen
import com.a401.spicoandroid.presentation.practice.screen.FinalSettingScreen
import com.a401.spicoandroid.presentation.practice.screen.ModeSelectScreen
import com.a401.spicoandroid.presentation.practice.screen.ProjectSelectScreen
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel
import com.a401.spicoandroid.presentation.project.screen.ProjectDetailScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectListScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectScriptInputScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectCreateScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectScriptDetailScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectScriptEditScreen
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectFormViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectViewModel
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechLandingScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechProjectListScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechReadyScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechSettingScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechTopicSelectScreen
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechSharedViewModel
import com.a401.spicoandroid.presentation.report.screen.VideoReplayScreen
import com.a401.spicoandroid.presentation.report.screen.CoachingReportScreen
import com.a401.spicoandroid.presentation.report.screen.FinalReportScreen
import com.a401.spicoandroid.presentation.report.screen.RandomSpeechReportScreen
import com.a401.spicoandroid.presentation.report.screen.VoiceScriptScreen
import kotlin.math.log

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavControllerProvider(navController = navController) {
        val weeklyCalendarViewModel: WeeklyCalendarViewModel = hiltViewModel()
        val projectViewModel: ProjectViewModel = hiltViewModel()
        val practiceViewModel: PracticeViewModel = hiltViewModel()
        val loginViewModel: LoginViewModel = hiltViewModel()
        val randomSpeechViewModel: RandomSpeechSharedViewModel = hiltViewModel()
        val projectFormViewModel: ProjectFormViewModel = hiltViewModel()


        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route,
            modifier = modifier
        ) {
            composable(NavRoutes.Home.route) {
                HomeScreen(
                    navController = navController,
                    modifier = modifier,
                    calendarViewModel = weeklyCalendarViewModel,
                    practiceViewModel = practiceViewModel
                )
            }

            // 마이페이지
            composable(NavRoutes.Profile.route){
                MyPageScreen(navController, modifier)
            }

            composable(NavRoutes.ProjectCreate.route) {
                ProjectCreateScreen(
                    navController = navController,
                    modifier = modifier,
                    viewModel = projectFormViewModel
                )
            }

            composable(NavRoutes.ProjectScriptInput.route) {
                ProjectScriptInputScreen(navController, modifier, projectFormViewModel)
            }

            composable(NavRoutes.ProjectList.route) {
                ProjectListScreen(
                    navController = navController,
                    projectViewModel = projectViewModel,
                    projectFormViewModel = projectFormViewModel,
                )
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

            composable(
                route = NavRoutes.ProjectDetail.route,
                arguments = listOf(navArgument("projectId") { type = NavType.IntType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: -1
                ProjectDetailScreen(
                    navController = navController,
                    projectId = projectId
                )
            }
            // 연습하기
            composable(NavRoutes.ModeSelect.route) {
                ModeSelectScreen(
                    navController = navController,
                    viewModel = practiceViewModel
                )
            }

            composable(route = NavRoutes.ProjectSelect.route) {
                ProjectSelectScreen(
                    navController = navController,
                    viewModel = practiceViewModel
                )
            }
            // 파이널 모드 설정 화면
            composable(NavRoutes.FinalSetting.route) {
                FinalSettingScreen(navController = navController,
                    viewModel = practiceViewModel
                )
            }
            composable(NavRoutes.FinalScreenCheck.route) {
                FinalScreenCheckScreen(
                    navController = navController,
                    viewModel = practiceViewModel
                )
            }
            // 랜덤 스피치
            composable(NavRoutes.RandomSpeechLanding.route) {
                RandomSpeechLandingScreen()
            }

            composable(NavRoutes.RandomSpeechTopicSelect.route) {
                RandomSpeechTopicSelectScreen(
                    navController = navController,
                    viewModel = randomSpeechViewModel
                )
            }

            composable(NavRoutes.RandomSpeechSetting.route) {
                RandomSpeechSettingScreen(
                    navController = navController,
                    viewModel = randomSpeechViewModel
                )
            }

            composable(NavRoutes.RandomSpeechReady.route) {
                RandomSpeechReadyScreen(
                    viewModel = randomSpeechViewModel,
                    onEndClick = {
                        navController.navigate(NavRoutes.RandomSpeechLanding.route) {
                            popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = true }
                        }
                    },
                    onStartClick = { _, _ ->
                        navController.navigate(NavRoutes.RandomSpeech.route)
                    }
                )
            }

            composable(NavRoutes.RandomSpeech.route) {
                RandomSpeechScreen(
                    viewModel = randomSpeechViewModel,
                    navController = navController,
                    onFinish = {
                        navController.navigate(NavRoutes.RandomSpeechLanding.route) {
                            popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = true }
                        }
                    }
                )
            }

            // 랜덤 스피치 리포트
            composable(
                route = NavRoutes.RandomSpeechReport.route,
                arguments = listOf(navArgument("randomSpeechId") { type = NavType.IntType })
            ) { backStackEntry ->
                val randomSpeechId = backStackEntry.arguments?.getInt("randomSpeechId") ?: -1
                RandomSpeechReportScreen(
                    navController = navController,
                    randomSpeechId = randomSpeechId
                )
            }


            // 코칭 모드
            composable(NavRoutes.CoachingMode.route) {
                CoachingModeScreen(navController)
            }
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
                route = NavRoutes.FinalModeQnA.route
            ) {
                FinalModeQnAScreen(navController = navController)
            }
            composable("final_mode_report") {
                FinalReportScreen(navController = navController)
            }
            composable(NavRoutes.VoiceScript.route) {
                VoiceScriptScreen(navController = navController)
            }
            composable(NavRoutes.VideoReplay.route) {
                VideoReplayScreen(navController = navController)
            }


            // 로그인
            composable(NavRoutes.Login.route) {
                LoginScreen(loginViewModel)
            }

            // 에러
            composable(NavRoutes.NotFound.route) {
                NotFoundScreen(navController)
            }

            // stt 테스트 스크린
            composable(NavRoutes.SpeechTest.route) {
                SpeechTestScreen(navController)
            }

        }
    }
}