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
import com.a401.spicoandroid.presentation.home.screen.HomeScreen
import com.a401.spicoandroid.presentation.home.viewmodel.WeeklyCalendarViewModel
import com.a401.spicoandroid.presentation.practice.screen.FinalSettingScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectDetailScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectListScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectSettingScreen
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectViewModel
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechLandingScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechProjectListScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechReadyScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechSettingScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechTopicSelectScreen
import com.a401.spicoandroid.presentation.report.screen.VideoReplayScreen

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

            composable(NavRoutes.ProjectCreate.route) {
                ProjectSettingScreen(navController, modifier)
            }

            composable(NavRoutes.ProjectList.route) {
                ProjectListScreen(navController, projectViewModel, {})
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
            // 연습 하기
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
                RandomSpeechTopicSelectScreen(
                    onNext = {
                        navController.navigate(NavRoutes.RandomSpeechSetting.route)
                    }
                )
            }
            composable(NavRoutes.RandomSpeechSetting.route) {
                RandomSpeechSettingScreen(
                    onNext = {
                        navController.navigate(NavRoutes.RandomSpeechReady.route)
                    }
                )
            }
            composable(NavRoutes.RandomSpeechReady.route) {
                RandomSpeechReadyScreen(
                    onNext = {
                        navController.navigate(NavRoutes.RandomSpeech.route)
                    }
                )
            }
            composable(NavRoutes.RandomSpeech.route) {
                RandomSpeechScreen(
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
                com.a401.spicoandroid.presentation.report.screen.RandomSpeechReportScreen(randomSpeechId = randomSpeechId)
            }

            // 로그인
            composable(NavRoutes.Login.route) {
                LoginScreen()
            }

        }
    }
}