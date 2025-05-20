package com.a401.spicoandroid.presentation.finalmode.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.infrastructure.camera.FinalRecordingCameraService
import com.a401.spicoandroid.presentation.finalmode.component.VideoBackgroundPlayer
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun FinalModeQnAScreen(
    navController: NavController,
    projectId: Int,
    practiceId: Int,
    viewModel: FinalModeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val countdown = viewModel.countdown
    val elapsedTime = viewModel.elapsedTime
    val showConfirm = viewModel.showStopConfirm

    val questionState by viewModel.finalQuestionState.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val scriptState by viewModel.scriptState.collectAsState()

    val cameraService = remember {
        FinalRecordingCameraService(
            context = context,
            lifecycleOwner = lifecycleOwner,
            script = "읽어야 하는 스크립트",
            isQuestionMode = true,
            onSttResult = { text ->
                // 현재 질문의 ID를 가져와서 updateAnswer 호출
                val currentQuestion = questionState.questions.getOrNull(currentIndex)
                currentQuestion?.let { question ->
                    viewModel.updateAnswer(questionId = question.id, answer = text)
                    Log.d("FinalFlow", "답변 업데이트: 질문 ID=${question.id}, 답변=$text")
                }
            }
        )

    }

    LaunchedEffect(Unit) {
        cameraService.startCamera {
            Log.d("FinalFlow", "🎥 QnA 카메라 준비 완료")
        }
    }

    // 초기 질문 수동 트리거
    LaunchedEffect(questionState.questions) {
        if (questionState.questions.isNotEmpty() &&
            viewModel.currentQuestionIndex.value == 0 &&
            !viewModel.isFirstQuestionStarted) {

            Log.d("TimerDebug", "🔥 초기 질문 0 수동 트리거 (질문 로드 이후)")
            viewModel.markFirstQuestionStarted()

            val fixedIndex = currentIndex

            cameraService.stopRecording {
                viewModel.startCountdownAndRecording {
                    viewModel.onQuestionStarted()
                    Log.d("FinalRecording", "📹 질문 ${fixedIndex + 1} 녹화 시작")
                    cameraService.startRecording(
                        projectId = projectId,
                        practiceId = practiceId,
                        fileTag = "qna${fixedIndex + 1}"
                    ) { uri ->
                        Log.d("FinalRecording", "✅ 질문 ${fixedIndex + 1} 저장 완료: $uri")
                    }
                }
            }
        }
    }

    LaunchedEffect(currentIndex) {
        Log.d("TimerDebug", "🎯 LaunchedEffect(currentIndex=$currentIndex) 실행됨")

        if (questionState.questions.isNotEmpty()) {
            val isFirst = currentIndex == 0 && !viewModel.isFirstQuestionStarted
            Log.d("TimerDebug", "⏸ 카메라 중지 시도")

            val fixedIndex = currentIndex

            cameraService.stopRecording {
                Log.d("TimerDebug", "▶️ startCountdownAndRecording 호출됨")
                viewModel.startCountdownAndRecording {
                    Log.d("TimerDebug", "⏱️ countdown 끝나고 onStartRecording 내부 진입")

                    if (isFirst) viewModel.markFirstQuestionStarted()
                    viewModel.onQuestionStarted()

                    Log.d("FinalRecording", "📹 질문 ${fixedIndex + 1} 녹화 시작")

                    cameraService.startRecording(
                        projectId = projectId,
                        practiceId = practiceId,
                        fileTag = "qna${fixedIndex + 1}"
                    ) { uri ->
                        Log.d("FinalRecording", "✅ 질문 ${fixedIndex + 1} 저장 완료: $uri")
                    }
                }
            }
        }
    }


    // 뒤로 가기 막기
    BackHandler(enabled = true) {
        viewModel.showConfirmDialog()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VideoBackgroundPlayer(
            context = context,
            videoResId = R.raw.final_qna,
            modifier = Modifier.fillMaxSize()
        )

        when {
            questionState.isLoading -> {
                Box(modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)) {
                    CommonFeedback(FeedbackType.FinalModeQnA("질문을 생성하고 있어요..."))
                }
            }
            questionState.error != null -> {
                Box(modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)) {
                    CommonFeedback(FeedbackType.FinalModeQnA("질문 생성에 실패했어요"))
                }
            }
            questionState.questions.isNotEmpty() -> {
                val currentQuestion = questionState.questions.getOrNull(currentIndex)
                currentQuestion?.let {
                    Box(modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)) {
                        CommonFeedback(FeedbackType.FinalModeQnA(it.text))
                    }
                }
            }
        }

        if (countdown >= 0) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                CommonTimer(timeText = countdown.toString(), type = TimerType.CIRCLE)
            }
        }

        if (countdown < 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) {
                CommonTimer(timeText = elapsedTime, type = TimerType.CHIP_LARGE)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
        ) {
            CommonButton(
                text = "종료",
                backgroundColor = Error,
                borderColor = Error,
                textColor = White,
                size = ButtonSize.SM,
                onClick = { viewModel.showConfirmDialog() },
                enabled = countdown < 0
            )
        }

        if (showConfirm) {
            CommonAlert(
                title = "Q&A를 종료하시겠습니까?",
                confirmText = "종료",
                onConfirm = {
                    viewModel.stopRecording()
                    viewModel.stopAudio()
                    viewModel.hideConfirmDialog()

                    Log.d("FinalFlow", "🧠 QnA 종료: projectId=$projectId, practiceId=$practiceId")

                    navController.navigate(
                        NavRoutes.FinalModeLoading.withArgs(
                            FinalModeLoadingType.REPORT,
                            projectId,
                            practiceId
                        )
                    )
                },
                confirmTextColor = White,
                confirmBackgroundColor = Error,
                confirmBorderColor = Error,
                cancelText = "취소",
                onCancel = { viewModel.hideConfirmDialog() },
                cancelTextColor = TextTertiary,
                cancelBackgroundColor = BackgroundSecondary,
                cancelBorderColor = BackgroundSecondary,
                borderColor = White,
                onDismissRequest = { viewModel.hideConfirmDialog() }
            )
        }
    }
}
