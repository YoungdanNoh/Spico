package com.a401.spicoandroid.presentation.report.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.report.component.*
import com.a401.spicoandroid.presentation.report.viewmodel.RandomReportViewModel

@Composable
fun RandomSpeechReportScreen(
    navController: NavController,
    randomSpeechId: Int,
    viewModel: RandomReportViewModel
) {
    val report = viewModel.report
    val showDeleteAlert = remember { mutableStateOf(false) }
    val context = LocalContext.current

    // 삭제 성공 시 화면 이동
    LaunchedEffect(viewModel.deleteSuccess) {
        if (viewModel.deleteSuccess) {
            navController.navigate(NavRoutes.RandomSpeechList.route) {
                popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = false }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchReport(randomSpeechId)
    }

    if (report == null) {
        LoadingInProgressView(
            imageRes = R.drawable.character_home_5,
            message = "리포트를 불러오고 있어요.\n잠시만 기다려주세요!",
            onHomeClick = { navController.navigate(NavRoutes.Home.route) }
        )
        return
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "리포트",
                leftContent = {
                    BackIconButton {
                        navController.navigate(NavRoutes.RandomSpeechList.route) {
                            popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = false }
                        }
                    }
                },
                rightContent = {
                    CommonButton(
                        text = "삭제",
                        size = ButtonSize.XS,
                        backgroundColor = White,
                        borderColor = Error,
                        textColor = Error,
                        onClick = { showDeleteAlert.value = true }
                    )
                }
            )
        },
        containerColor = BrokenWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            RandomReportHeader(title = report.title, topic = report.topic)
            RandomReportQuestionSection(question = report.question)
            RandomReportNewsSection(
                title = report.newsTitle,
                summary = report.newsSummary,
                url = report.newsUrl,
                context = context
            )
            RandomReportFeedbackSection(feedback = report.feedback)
            RandomReportScriptButton(
                onClick = {
                    navController.navigate(NavRoutes.VoiceScriptRandom.withId(randomSpeechId))
                },
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        if (showDeleteAlert.value) {
            RandomReportDeleteAlert(
                onConfirm = {
                    showDeleteAlert.value = false
                    viewModel.deleteReport(randomSpeechId)
                },
                onCancel = { showDeleteAlert.value = false },
                onDismiss = { showDeleteAlert.value = false }
            )
        }
    }
}

