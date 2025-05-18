package com.a401.spicoandroid.presentation.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
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
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectDetailViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectFormViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectScriptViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectViewModel
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechLandingScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechListScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechReadyScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechSettingScreen
import com.a401.spicoandroid.presentation.randomspeech.screen.RandomSpeechTopicSelectScreen
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechListViewModel
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechSharedViewModel
import com.a401.spicoandroid.presentation.report.screen.VideoReplayScreen
import com.a401.spicoandroid.presentation.report.screen.CoachingReportScreen
import com.a401.spicoandroid.presentation.report.screen.FinalReportScreen
import com.a401.spicoandroid.presentation.report.screen.RandomSpeechReportScreen
import com.a401.spicoandroid.presentation.report.screen.VoiceScriptRandomScreen
import com.a401.spicoandroid.presentation.report.screen.VoiceScriptScreen
import com.a401.spicoandroid.presentation.report.viewmodel.RandomReportViewModel
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
        val randomSpeechListViewModel: RandomSpeechListViewModel = hiltViewModel()
        val projectDetailViewModel: ProjectDetailViewModel = hiltViewModel()
        val projectScriptViewModel: ProjectScriptViewModel = hiltViewModel()
        
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route,
            modifier = modifier
        ) {
            // 홈
            composable(NavRoutes.Home.route) {
                HomeScreen(
                    navController = navController,
                    modifier = modifier,
                    calendarViewModel = weeklyCalendarViewModel,
                    practiceViewModel = practiceViewModel
                )
            }

            composable(
                route = NavRoutes.HomeCoachingReportDetail.route,
                arguments = listOf(
                    navArgument("projectId") { type = NavType.IntType },
                    navArgument("practiceId") { type = NavType.IntType },
                    navArgument("source") { type = NavType.StringType; defaultValue = "home" }
                )
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: return@composable
                val practiceId = backStackEntry.arguments?.getInt("practiceId") ?: return@composable
                val source = backStackEntry.arguments?.getString("source") ?: "home"

                CoachingReportScreen(
                    navController = navController,
                    projectId = projectId,
                    practiceId = practiceId
                )
            }

            composable(
                route = NavRoutes.HomeFinalReportDetail.route,
                arguments = listOf(
                    navArgument("projectId") { type = NavType.IntType },
                    navArgument("practiceId") { type = NavType.IntType },
                    navArgument("source") { type = NavType.StringType; defaultValue = "home" }
                )
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: return@composable
                val practiceId = backStackEntry.arguments?.getInt("practiceId") ?: return@composable
                val source = backStackEntry.arguments?.getString("source") ?: "home"

                FinalReportScreen(
                    navController = navController,
                    projectId = projectId,
                    practiceId = practiceId
                )
            }
            // 프로필
            composable(NavRoutes.Profile.route){
                MyPageScreen(navController, modifier)
            }

            composable(
                route = NavRoutes.ProjectCreate.routeWithReset,
                arguments = listOf(
                    navArgument("reset") { defaultValue = "false" }
                )
            ) { backStackEntry ->
                val reset = backStackEntry.arguments?.getString("reset")?.toBooleanStrictOrNull() ?: false

                ProjectCreateScreen(
                    navController = navController,
                    modifier = modifier,
                    viewModel = projectFormViewModel,
                    reset = reset
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
                    projectDetailViewModel,
                    projectScriptViewModel,
                    onEdit = {
                        navController.navigate(NavRoutes.ProjectScriptEdit.route)
                    }
                )
            }

            composable(NavRoutes.ProjectScriptEdit.route) {
                ProjectScriptEditScreen(
                    navController,
                    projectDetailViewModel,
                    projectScriptViewModel
                )
            }

            composable(
                route = NavRoutes.ProjectDetail.route,
                arguments = listOf(navArgument("projectId") { type = NavType.IntType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: -1
                ProjectDetailScreen(
                    navController = navController,
                    viewModel = projectDetailViewModel,
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
                    viewModel = practiceViewModel,
                    scriptViewModel = projectScriptViewModel,
                    detailViewModel = projectDetailViewModel
                )
            }
            // 파이널 모드 설정 화면
            composable(NavRoutes.FinalSetting.route) {
                FinalSettingScreen(navController = navController,
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

            composable(NavRoutes.RandomSpeechList.route) {
                RandomSpeechListScreen(
                    viewModel = randomSpeechListViewModel,
                    onProjectClick = { id ->
                        navController.navigate(NavRoutes.RandomSpeechReport.withId(id))
                    }
                )
            }

            // 랜덤 스피치 리포트
            composable(
                route = NavRoutes.RandomSpeechReport.route,
                arguments = listOf(navArgument("randomSpeechId") { type = NavType.IntType })
            ) { backStackEntry ->
                val reportViewModel: RandomReportViewModel = hiltViewModel()
                val randomSpeechId = backStackEntry.arguments?.getInt("randomSpeechId") ?: -1
                RandomSpeechReportScreen(
                    navController = navController,
                    randomSpeechId = randomSpeechId,
                    viewModel = reportViewModel
                )
            }

            composable(
                route = NavRoutes.VoiceScriptRandom.route,
                arguments = listOf(navArgument("randomSpeechId") { type = NavType.IntType })
            ) { backStackEntry ->
                val randomSpeechId = backStackEntry.arguments?.getInt("randomSpeechId") ?: return@composable
                VoiceScriptRandomScreen(navController = navController, randomSpeechId = randomSpeechId)
            }

            // 코칭 모드
            composable(
                route = NavRoutes.CoachingMode.route,
                arguments = listOf(
                    navArgument("projectId") { type = NavType.IntType },
                    navArgument("practiceId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: return@composable
                val practiceId = backStackEntry.arguments?.getInt("practiceId") ?: return@composable

                CoachingModeScreen(
                    navController = navController,
                    projectId = projectId,
                    practiceId = practiceId,
                    scriptViewModel = projectScriptViewModel
                )
            }

            composable(
                NavRoutes.CoachingReport.route,
                arguments = listOf(
                    navArgument("projectId") { type = NavType.IntType },
                    navArgument("practiceId") { type = NavType.IntType }
                )
            ){
                backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: return@composable
                val practiceId = backStackEntry.arguments?.getInt("practiceId") ?: return@composable
                CoachingReportScreen(navController, projectId, practiceId)
            }

            // 파이널 모드
            composable(NavRoutes.FinalModeRoot.route) {
                val parentEntry =
                    remember { navController.getBackStackEntry(NavRoutes.FinalModeRoot.route) }
                val sharedViewModel: FinalModeViewModel = hiltViewModel(parentEntry)
                val childNavController = rememberNavController()

                NavHost(
                    navController = childNavController,
                    startDestination = "final_check"
                ) {
                    composable("final_check") {
                        FinalScreenCheckScreen(
                            navController = childNavController,
                            parentNavController = navController,
                            practiceViewModel = practiceViewModel,
                            finalModeViewModel = sharedViewModel
                        )
                    }
                    composable(
                        "final_mode_voice/{projectId}/{practiceId}",
                        arguments = listOf(
                            navArgument("projectId") { type = NavType.IntType },
                            navArgument("practiceId") { type = NavType.IntType }
                        )
                    ) {
                        val args = it.arguments!!
                        FinalModeVoiceScreen(
                            navController = childNavController,
                            parentNavController = navController,
                            projectId = args.getInt("projectId"),
                            practiceId = args.getInt("practiceId"),
                            viewModel = sharedViewModel
                        )
                    }
                    composable(
                        "final_mode_audience/{projectId}/{practiceId}",
                        arguments = listOf(
                            navArgument("projectId") { type = NavType.IntType },
                            navArgument("practiceId") { type = NavType.IntType }
                        )
                    ) {
                        val args = it.arguments!!
                        FinalModeAudienceScreen(
                            navController = childNavController,
                            projectId = args.getInt("projectId"),
                            practiceId = args.getInt("practiceId"),
                            viewModel = sharedViewModel
                        )
                    }
                    composable(
                        "final_mode_loading/{type}/{projectId}/{practiceId}",
                        arguments = listOf(
                            navArgument("type") { type = NavType.StringType },
                            navArgument("projectId") { type = NavType.IntType },
                            navArgument("practiceId") { type = NavType.IntType }
                        )
                    ) {
                        val args = it.arguments!!
                        FinalModeLoadingScreen(
                            navController = childNavController,
                            parentNavController = navController,
                            type = FinalModeLoadingType.valueOf(args.getString("type")!!),
                            projectId = args.getInt("projectId"),
                            practiceId = args.getInt("practiceId"),
                            viewModel = sharedViewModel
                        )
                    }
                    composable(
                        "final_mode_qna/{projectId}/{practiceId}",
                        arguments = listOf(
                            navArgument("projectId") { type = NavType.IntType },
                            navArgument("practiceId") { type = NavType.IntType }
                        )
                    ) {
                        val args = it.arguments!!
                        FinalModeQnAScreen(
                            navController = childNavController,
                            projectId = args.getInt("projectId"),
                            practiceId = args.getInt("practiceId"),
                            viewModel = sharedViewModel
                        )
                    }
                }
            }

            // 파이널 리포트
            composable(
                route = NavRoutes.FinalReport.route,
                arguments = listOf(
                    navArgument("projectId") { type = NavType.IntType },
                    navArgument("practiceId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: return@composable
                val practiceId = backStackEntry.arguments?.getInt("practiceId") ?: return@composable

                Log.d("NavGraph", "✅ FinalReport 라우트 진입: $projectId / $practiceId")

                FinalReportScreen(
                    navController = navController,
                    projectId = projectId,
                    practiceId = practiceId
                )
            }


            // 로그인
            composable(NavRoutes.Login.route) {
                LoginScreen(
                    navController = navController,
                    loginViewModel = hiltViewModel())
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