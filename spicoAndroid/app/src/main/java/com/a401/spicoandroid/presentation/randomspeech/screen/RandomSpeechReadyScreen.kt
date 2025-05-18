package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.openExternalLink
import com.a401.spicoandroid.presentation.randomspeech.component.RandomSpeechExitAlert
import com.a401.spicoandroid.presentation.randomspeech.component.RandomSpeechReadyTimerSection
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechSharedViewModel

@Composable
fun RandomSpeechReadyScreen(
    onEndClick: () -> Unit = {},
    onStartClick: (question: String, speakMin: Int) -> Unit,
    viewModel: RandomSpeechSharedViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showExitAlert by remember { mutableStateOf(false) }

    val question = uiState.question
    val newsTitle = uiState.newsTitle
    val newsSummary = uiState.newsSummary
    val newsUrl = uiState.newsUrl
    val prepMin = uiState.prepTime / 60
    val speakMin = uiState.speakTime / 60

    BackHandler(enabled = true) {
        showExitAlert = true
    }

    Scaffold(
        topBar = {
            CommonTopBar(centerText = "랜덤스피치")
        },
        bottomBar = {
            CommonButton(
                text = "바로 시작하기",
                size = ButtonSize.LG,
                onClick = { onStartClick(question, speakMin) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(BrokenWhite)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.character_random),
                    contentDescription = "캐릭터",
                    modifier = Modifier.size(100.dp)
                )

                RandomSpeechReadyTimerSection(
                    prepMin = prepMin,
                    onFinish = { onStartClick(question, speakMin) }
                )
            }

            InfoSection(title = "랜덤스피치 질문") {
                Text(question, style = Typography.bodyLarge, color = TextPrimary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (newsTitle.isNotBlank() && newsSummary.isNotBlank() && newsUrl.isNotBlank()) {
                val maxLength = 300
                val trimmedSummary = if (newsSummary.length > maxLength) {
                    newsSummary.substring(0, maxLength) + "..."
                } else {
                    newsSummary
                }

                InfoSection(title = "관련기사") {
                    Text(newsTitle, style = Typography.displaySmall, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(trimmedSummary, style = Typography.titleMedium, color = TextTertiary)
                    Spacer(modifier = Modifier.height(12.dp))
                    CommonButton(
                        text = "기사 원문 확인하기",
                        size = ButtonSize.LG,
                        backgroundColor = White,
                        borderColor = Action,
                        textColor = Action,
                        onClick = { openExternalLink(context, newsUrl) }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        if (showExitAlert) {
            RandomSpeechExitAlert(
                onDismissRequest = { showExitAlert = false },
                onCancel = { showExitAlert = false },
                onConfirm = {
                    showExitAlert = false
                    onEndClick()
                }
            )
        }
    }
}


