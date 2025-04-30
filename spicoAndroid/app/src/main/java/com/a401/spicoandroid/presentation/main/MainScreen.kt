package com.a401.spicoandroid.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a401.spicoandroid.presentation.main.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

}