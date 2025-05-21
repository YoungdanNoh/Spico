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

    // UI 내에서 백업 상태 기억
    var backupQuestionCount by remember { mutableIntStateOf(viewModel.questionCount) }
    var backupAnswerTime by remember { mutableIntStateOf(viewModel.answerTimeLimit) }


    // 에러 상태
    var questionError by remember { mutableStateOf(false) }
    var answerTimeError by remember { mutableStateOf(false) }

    // 화면 진입 시 서버에서 설정값 불러오기
    LaunchedEffect(Unit) {
        viewModel.fetchFinalSetting()
    }

    LaunchedEffect(setting) {
        setting?.let {
            hasAudience = it.hasAudience
            hasQnA = it.hasQnA
            val safeQuestion = maxOf(it.questionCount, 1)
            val safeAnswer = maxOf(it.answerTimeLimit, 30)

            questionCount = safeQuestion
            answerTimeSec = safeAnswer

            backupQuestionCount = safeQuestion
            backupAnswerTime = safeAnswer

            viewModel.updateQuestionCount(safeQuestion)
            viewModel.updateAnswerTimeLimit(safeAnswer)
        }
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
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonButton(
                    text = "다음",
                    size = ButtonSize.LG,
                    onClick = {
                        val safeQuestionCount = if (hasQnA) questionCount else backupQuestionCount
                        val safeAnswerTime = if (hasQnA) answerTimeSec else backupAnswerTime

                        // 안전한 값 ViewModel에 저장
                        viewModel.hasAudience = hasAudience
                        viewModel.hasQnA = hasQnA
                        viewModel.questionCount = safeQuestionCount
                        viewModel.answerTimeLimit = safeAnswerTime

                        viewModel.updateQuestionCount(safeQuestionCount)
                        viewModel.updateAnswerTimeLimit(safeAnswerTime)

                        viewModel.saveFinalSetting(
                            onSuccess = {
                                navController.navigate(NavRoutes.FinalModeRoot.route)
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
                onToggle = {
                    hasQnA = it
                    viewModel.hasQnA = it

                    if (it) {
                        questionCount = backupQuestionCount
                        answerTimeSec = backupAnswerTime
                    } else {
                        backupQuestionCount = questionCount
                        backupAnswerTime = answerTimeSec
                    }
                }
            )

            // 질문 개수 (최소 1, 최대 3)
            SettingStepperItem(
                title = "질문 개수",
                valueText = "${questionCount}개",
                onIncrement = {
                    if (questionCount < 3) {
                        questionCount++
                        viewModel.updateQuestionCount(questionCount)
                        questionError = false
                    } else {
                        questionError = true
                    }
                },
                onDecrement = {
                    if (questionCount > 1) {
                        questionCount--
                        viewModel.updateQuestionCount(questionCount)
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
                        viewModel.updateAnswerTimeLimit(answerTimeSec)
                        answerTimeError = false
                    } else {
                        answerTimeError = true
                    }
                },
                onDecrement = {
                    if (answerTimeSec > 30) {
                        answerTimeSec -= 30
                        viewModel.updateAnswerTimeLimit(answerTimeSec)
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
