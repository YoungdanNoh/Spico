package com.a401.spicoandroid.presentation.report.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.report.viewmodel.RandomReportViewModel

@Composable
fun VoiceScriptRandomScreen(
    navController: NavController,
    randomSpeechId: Int,
    viewModel: RandomReportViewModel = hiltViewModel()
) {
    val report = viewModel.report
    val isLoading = viewModel.isLoading

    // fetch
    LaunchedEffect(Unit) {
        viewModel.fetchReport(randomSpeechId)
    }

    // 뒤로 가기
    BackHandler {
        navController.popBackStack()
    }

    if (isLoading || report == null) {
        LoadingInProgressView(
            imageRes = R.drawable.character_home_5,
            message = "스크립트를 불러오고 있어요.",
            onHomeClick = { navController.navigate(NavRoutes.RandomSpeechList.route) }
        )
        return
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "음성 스크립트",
                leftContent = {
                    BackIconButton {
                        navController.popBackStack()
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                CommonButton(
                    text = "리포트 보기",
                    onClick = {
                        navController.popBackStack() // 리포트 화면으로 되돌아가기
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = report.title,
                style = Typography.displayMedium,
                color = TextPrimary
            )
            Text(
                text = report.script,
                style = Typography.titleLarge,
                color = TextPrimary
            )
        }
    }
}
