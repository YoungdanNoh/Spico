package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.openExternalLink
import com.a401.spicoandroid.presentation.randomspeech.component.RandomSpeechExitAlert
import com.a401.spicoandroid.presentation.randomspeech.component.RandomSpeechReadyTimerSection
import com.a401.spicoandroid.presentation.randomspeech.dummy.DummyRandomSpeechReady

@Composable
fun RandomSpeechReadyScreen(
    prepMin: Int,
    speakMin: Int,
    onEndClick: () -> Unit = {},
    onStartClick: (question: String, speakMin: Int) -> Unit
) {
    val context = LocalContext.current
    val data = DummyRandomSpeechReady
    var showExitAlert by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "랜덤스피치",
                rightContent = {
                    CommonButton(
                        text = "종료",
                        size = ButtonSize.XS,
                        backgroundColor = Error,
                        borderColor = Error,
                        textColor = White,
                        onClick = { showExitAlert = true }
                    )
                }
            )
        },
        bottomBar = {
            CommonButton(
                text = "바로 시작하기",
                size = ButtonSize.LG,
                onClick = {
                    onStartClick(data.question, speakMin)
                },
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
                    onFinish = { onStartClick(data.question, speakMin) }
                )
            }

            InfoSection(title = "랜덤스피치 질문") {
                Text(data.question, style = Typography.bodyLarge, color = TextPrimary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(title = "관련기사") {
                Text(data.newsTitle, style = Typography.displaySmall, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(data.newsSummary, style = Typography.titleMedium, color = TextTertiary)
                Spacer(modifier = Modifier.height(12.dp))
                CommonButton(
                    text = "기사 원문 확인하기",
                    size = ButtonSize.LG,
                    backgroundColor = White,
                    borderColor = Action,
                    textColor = Action,
                    onClick = { openExternalLink(context, data.newsUrl) }
                )
            }

            Spacer(modifier = Modifier.height(100.dp)) // 버튼 위 여백 확보
        }

        // 다이얼로그 조건부 렌더링
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

