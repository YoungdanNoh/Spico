package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.a401.spicoandroid.presentation.main.MainScreen
import com.a401.spicoandroid.presentation.main.viewmodel.MainViewModel
import com.a401.spicoandroid.presentation.mypage.screen.MyPageScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectDetailScreen
import com.a401.spicoandroid.presentation.project.screen.ProjectListScreen
import com.a401.spicoandroid.presentation.project.viewmodel.Project

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavControllerProvider(navController = navController) {
        val mainViewModel: MainViewModel = hiltViewModel()

        NavHost(
            navController = navController,
            startDestination = NavRoutes.Main.route,
            modifier = modifier
        ) {
            composable(NavRoutes.Main.route) {
                MainScreen(navController, modifier)
            }
            composable(NavRoutes.ProjectList.route) {
                ProjectListScreen(
                    navController = navController,
                    onFabClick = {}
                )
            }
            composable(
                route = NavRoutes.ProjectDetail.route,
                arguments = listOf(
                    navArgument("project") {
                        type = NavType.ParcelableType(Project::class.java)
                    }
                )
            ) { backStackEntry ->
                val project = backStackEntry.arguments?.getParcelable<Project>("project")
                if(project != null) {
                    ProjectDetailScreen(
                        navController = navController,
                        onFabClick = {},
                        project = project
                    )
                }
            }
            composable(route = NavRoutes.Profile.route) {
                MyPageScreen(
                    navController = navController,
                    onFabClick = {},
                    onWithdraw = {},
                    onLogout = {},
                )
            }
        }
    }
}