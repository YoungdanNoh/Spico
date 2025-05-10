package com.a401.spicoandroid.presentation.report.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.openExternalLink
import com.a401.spicoandroid.presentation.report.component.RandomReportDeleteAlert
import com.a401.spicoandroid.presentation.report.component.RandomReportHeader
import com.a401.spicoandroid.presentation.report.dummy.DummyRandomSpeechReport
import com.a401.spicoandroid.common.ui.component.InfoSection

@Composable
fun RandomSpeechReportScreen(
    navController: NavController,
    randomSpeechId: Int
) {
    val report = remember { DummyRandomSpeechReport }
    val showDeleteAlert = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "리포트",
                leftContent = { BackIconButton(navController) },
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

            InfoSection(title = "랜덤스피치 질문") {
                Text(
                    text = report.question,
                    style = Typography.bodyLarge,
                    color = TextPrimary
                )
            }

            InfoSection(title = "관련기사") {
                Text(report.newsTitle, style = Typography.displaySmall, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(report.newsSummary, style = Typography.titleMedium, color = TextTertiary)
                Spacer(modifier = Modifier.height(12.dp))
                CommonButton(
                    text = "기사 원문 확인하기",
                    size = ButtonSize.LG,
                    backgroundColor = White,
                    borderColor = Action,
                    textColor = Action,
                    onClick = { openExternalLink(context, report.newsUrl) }
                )
            }

            InfoSection(title = "피드백") {
                Text(
                    text = report.feedback,
                    style = Typography.bodyLarge,
                    color = TextPrimary
                )
            }

            CommonButton(
                text = "음성 스크립트",
                size = ButtonSize.LG,
                onClick = { /* 음성 스크립트 이동 */ }
            )
        }

        if (showDeleteAlert.value) {
            RandomReportDeleteAlert(
                onConfirm = {
                    showDeleteAlert.value = false
                    println("리포트 삭제됨") // TODO: 실제 삭제 로직 추가
                },
                onCancel = { showDeleteAlert.value = false },
                onDismiss = { showDeleteAlert.value = false }
            )
        }
    }
}
