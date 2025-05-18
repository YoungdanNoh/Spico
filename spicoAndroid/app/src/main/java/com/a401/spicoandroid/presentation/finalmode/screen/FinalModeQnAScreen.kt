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

    val cameraService = remember {
        FinalRecordingCameraService(context, lifecycleOwner)
    }

    // TODO: STT 결과로 대체 예정
    val dummySpeechContent = "Hello everyone, my name is John."

    LaunchedEffect(Unit) {
//        viewModel.generateFinalQuestions(
//            projectId = projectId,
//            practiceId = practiceId,
//            speechContent = dummySpeechContent
//        )

        cameraService.startCamera {
            viewModel.startCountdownAndRecording {
                cameraService.startRecording { uri ->
                    Log.d("FinalRecording", "저장 완료: $uri")
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
