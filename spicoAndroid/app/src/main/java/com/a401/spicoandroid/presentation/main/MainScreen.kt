package com.a401.spicoandroid.presentation.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a401.spicoandroid.presentation.main.viewmodel.MainViewModel
import java.lang.reflect.Modifier

@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

}