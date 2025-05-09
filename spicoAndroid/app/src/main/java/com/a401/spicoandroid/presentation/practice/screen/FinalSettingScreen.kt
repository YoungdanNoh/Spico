package com.a401.spicoandroid.presentation.practice.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.practice.component.SettingStepperItem
import com.a401.spicoandroid.presentation.practice.component.SettingToggleItem

@Composable
fun FinalSettingScreen(
    navController: NavController
) {
    var hasAudience by remember { mutableStateOf(true) }
    var hasQnA by remember { mutableStateOf(true) }
    var questionCount by remember { mutableStateOf(1) }
    var answerTimeSec by remember { mutableStateOf(90) } // 1분 30초

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "발표 설정",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        onClick = { navController.popBackStack() }
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonButton(
                    text = "다음",
                    size = ButtonSize.LG,
                    onClick = { /* 다음 화면 이동 */ }
                )
            }
        }
        ,
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // 청중 여부
            SettingToggleItem(
                title = "청중 여부",
                checked = hasAudience,
                onToggle = { hasAudience = it }
            )

            // 질의응답 여부
            SettingToggleItem(
                title = "질의응답 여부",
                checked = hasQnA,
                onToggle = { hasQnA = it }
            )

            // 질문 개수 (최소 1, 최대 3)
            SettingStepperItem(
                title = "질문 개수",
                valueText = "${questionCount}개",
                onIncrement = { if (questionCount < 3) questionCount++ },
                onDecrement = { if (questionCount > 1) questionCount-- },
                showError = (questionCount <= 1),
                errorMessage = "질문 개수는 최소 1개입니다."
            )

            // 답변 시간 (30초~180초)
            val minutes = answerTimeSec / 60
            val seconds = answerTimeSec % 60
            val timeText = if (minutes > 0) "${minutes}분 ${seconds.toString().padStart(2, '0')}초" else "${seconds}초"

            SettingStepperItem(
                title = "답변 시간",
                valueText = timeText,
                onIncrement = { if (answerTimeSec < 180) answerTimeSec += 30 },
                onDecrement = { if (answerTimeSec > 30) answerTimeSec -= 30 },
                showError = (answerTimeSec > 180),
                errorMessage = "답변 시간은 최대 3분입니다."
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FAF8, widthDp = 360)
@Composable
fun PreviewFinalSettingScreen() {
    SpeakoAndroidTheme {
        FinalSettingScreen(navController = rememberNavController())
    }
}