package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import com.a401.spicoandroid.infrastructure.speech.SpeechTestScreen
import com.a401.spicoandroid.presentation.auth.screen.LoginScreen
import com.a401.spicoandroid.presentation.error.screen.NotFoundScreen

@Composable
fun LoginNavGraph(
    navController: NavHostController,
    userDataStore: UserDataStore,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login.route
    ) {
        //// 비인증 접근 가능////
        // 로그인
        composable(NavRoutes.Login.route) {
            LoginScreen(
                navController = navController,
                loginViewModel = hiltViewModel(),
                userDataStore = userDataStore
            )
        }

        // 에러
        composable(NavRoutes.NotFound.route) {
            NotFoundScreen(navController)
        }

    }
}
