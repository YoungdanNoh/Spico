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
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.formatTime
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.practice.component.SettingStepperItem
import com.a401.spicoandroid.presentation.practice.component.SettingToggleItem

@Composable
fun FinalSettingScreen(
    navController: NavController
) {
    var hasAudience by remember { mutableStateOf(true) }
    var hasQnA by remember { mutableStateOf(true) }
    var questionCount by remember { mutableIntStateOf(1) }
    var answerTimeSec by remember { mutableIntStateOf(90) } // 1분 30초

    // 에러 상태
    var questionError by remember { mutableStateOf(false) }
    var answerTimeError by remember { mutableStateOf(false) }

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
                    onClick = { navController.navigate(NavRoutes.FinalScreenCheck.route) }
                )
            }
        },
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
                onIncrement = {
                    if (questionCount < 3) {
                        questionCount++
                        questionError = false
                    } else {
                        questionError = true
                    }
                },
                onDecrement = {
                    if (questionCount > 1) {
                        questionCount--
                        questionError = false
                    } else {
                        questionError = true
                    }
                },
                enabled = hasQnA,
                showError = questionError,
                errorMessage = if (questionCount <= 1) "질문 개수는 최소 1개입니다." else "질문 개수는 최대 3개입니다."
            )

            // 답변 시간 (최소 30, 최대 180)
            SettingStepperItem(
                title = "답변 시간",
                valueText = formatTime(answerTimeSec),
                onIncrement = {
                    if (answerTimeSec < 180) {
                        answerTimeSec += 30
                        answerTimeError = false
                    } else {
                        answerTimeError = true
                    }
                },
                onDecrement = {
                    if (answerTimeSec > 30) {
                        answerTimeSec -= 30
                        answerTimeError = false
                    } else {
                        answerTimeError = true
                    }
                },
                enabled = hasQnA,
                showError = answerTimeError,
                errorMessage = if (answerTimeSec <= 30) "답변 시간은 최소 30초입니다." else "답변 시간은 최대 3분입니다."
            )
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFF7FAF8, widthDp = 360)
//@Composable
//fun PreviewFinalSettingScreen() {
//    SpeakoAndroidTheme {
//        FinalSettingScreen(navController = rememberNavController())
//    }
//}