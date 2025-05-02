package com.a401.spicoandroid.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.presentation.main.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CommonTopBar(modifier = Modifier,
                text = "메인화면에서 탑바",
                showBackButton = true,
                actionImageId = R.drawable.ic_launcher_foreground,
                onActionClick = {}
            )
        },
        bottomBar = {
            Text("바텀바")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text("메인 화면")
        }
    }
}