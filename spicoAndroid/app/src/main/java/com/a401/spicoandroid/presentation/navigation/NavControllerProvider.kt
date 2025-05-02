package com.a401.spicoandroid.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController

val LocalNavController = staticCompositionLocalOf<NavController> { error("NavController not provided") }

@Composable
fun NavControllerProvider(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalNavController provides navController) {
        content()
    }
}