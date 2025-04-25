package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.a401.spicoandroid.presentation.main.MainScreen
import com.a401.spicoandroid.presentation.main.viewmodel.MainViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavControllerProvider(navController = navController) {
        val mainViewModel: MainViewModel = hiltViewModel()
    }
}