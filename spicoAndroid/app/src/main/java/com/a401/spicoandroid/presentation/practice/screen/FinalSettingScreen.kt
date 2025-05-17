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
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.formatTime
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.practice.component.SettingStepperItem
import com.a401.spicoandroid.presentation.practice.component.SettingToggleItem
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel

@Composable
fun FinalSettingScreen(
    navController: NavController,
    viewModel: PracticeViewModel
) {
    // ViewModel
    val setting by viewModel.finalSetting.collectAsState()
    var hasAudience by remember { mutableStateOf(viewModel.hasAudience) }
    var hasQnA by remember { mutableStateOf(viewModel.hasQnA) }
    var questionCount by remember { mutableIntStateOf(viewModel.questionCount) }
    var answerTimeSec by remember { mutableIntStateOf(viewModel.answerTimeLimit) }

    // 에러 상태
    var questionError by remember { mutableStateOf(false) }
    var answerTimeError by remember { mutableStateOf(false) }

    // 화면 진입 시 서버에서 설정값 불러오기
    LaunchedEffect(Unit) {
        viewModel.fetchFinalSetting()
    }
    if (setting != null) {
        hasAudience = setting!!.hasAudience
        hasQnA = setting!!.hasQnA
        questionCount = setting!!.questionCount
        answerTimeSec = setting!!.answerTimeLimit
    }

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
                    onClick = {
                        viewModel.hasAudience = hasAudience
                        viewModel.hasQnA = hasQnA
                        viewModel.questionCount = questionCount
                        viewModel.answerTimeLimit = answerTimeSec

                        viewModel.saveFinalSetting(
                            onSuccess = {
                                navController.navigate(NavRoutes.FinalScreenCheck.route)
                            },
                            onFailure = {
                                // TODO: 에러
                            }
                        )
                    }
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
