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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.report.viewmodel.FinalReportViewModel

@Composable
fun VoiceScriptScreen(
    navController: NavController,
    projectId: Int,
    practiceId: Int,
    finalModeViewModel: FinalModeViewModel,
    finalReportViewModel: FinalReportViewModel
)
 {
     BackHandler {
         navController.navigate(NavRoutes.FinalReport.createRoute(projectId, practiceId))
     }

     LaunchedEffect(Unit) {
         finalReportViewModel.fetchFinalReport(projectId, practiceId)
     }

     val scrollState = rememberScrollState()
     val reportState by finalReportViewModel.state.collectAsState()

     Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "음성 스크립트",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로 가기",
                        onClick = {
                            navController.navigate(NavRoutes.FinalReport.createRoute(projectId, practiceId))
                        }
                    )
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
                        navController.navigate(NavRoutes.FinalReport.createRoute(projectId, practiceId))
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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = reportState.projectName,
                style = Typography.displayMedium,
                color = TextPrimary
            )

            Text(
                text = reportState.voiceScript?:"기록된 음성 스크립트가 없습니다.",
                style = Typography.titleLarge,
                color = TextPrimary
            )
        }
    }
}
