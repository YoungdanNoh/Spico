package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.a401.spicoandroid.presentation.home.screen.HomeScreen
import com.a401.spicoandroid.presentation.home.viewmodel.WeeklyCalendarViewModel
import com.a401.spicoandroid.presentation.mypage.screen.MyPageScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectDetailScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectListScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectSettingScreen
import com.a401.spicoandroid.presentation.project.viewmodel.Project

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavControllerProvider(navController = navController) {
        val weeklyCalendarViewModel: WeeklyCalendarViewModel = hiltViewModel()

        NavHost(
            navController = navController,
            startDestination = NavRoutes.Main.route,
            modifier = modifier
        ) {
            composable(NavRoutes.Main.route) {
                HomeScreen(navController, modifier, weeklyCalendarViewModel)
            }

            composable(NavRoutes.ProjectCreate.route) {
                ProjectSettingScreen(navController, modifier)
            }

            composable(NavRoutes.ProjectList.route) {
                ProjectListScreen(navController, {})
            }

            composable(
                route = NavRoutes.ProjectDetail.route,
                arguments = listOf(navArgument("projectId") { type = NavType.IntType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId") ?: -1
                ProjectDetailScreen(projectId = projectId)
            }
        }
    }
}