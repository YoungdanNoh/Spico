package com.a401.spicoandroid.presentation.finalmode.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.presentation.finalmode.component.VideoBackgroundPlayer
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonAlert
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonFeedback
import com.a401.spicoandroid.common.ui.component.CommonTimer
import com.a401.spicoandroid.common.ui.component.FeedbackType
import com.a401.spicoandroid.common.ui.component.TimerType
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.infrastructure.camera.FinalRecordingCameraService
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun FinalModeQnAScreen(
    navController: NavController,
    viewModel: FinalModeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val countdown = viewModel.countdown
    val elapsedTime = viewModel.elapsedTime
    val showConfirm = viewModel.showStopConfirm

    val cameraService = remember {
        FinalRecordingCameraService(context, lifecycleOwner)
    }

    val question = viewModel.question

    LaunchedEffect(Unit) {
        viewModel.fetchQuestion()
        cameraService.startCamera {
            viewModel.startCountdownAndRecording {
                cameraService.startRecording { uri ->
                    Log.d("FinalRecording", "저장 완료: $uri")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VideoBackgroundPlayer(
            context = context,
            videoResId = R.raw.final_qna,
            modifier = Modifier.fillMaxSize()
        )

        // 질문 텍스트
        if (question.isNotBlank()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                CommonFeedback(FeedbackType.FinalModeQnA(question))
            }
        }

        // 카운트다운
        if (countdown >= 0) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                CommonTimer(timeText = countdown.toString(), type = TimerType.CIRCLE)
            }
        }

        // 경과시간 (countdown 끝나면)
        if (countdown < 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) {
                CommonTimer(timeText = elapsedTime, type = TimerType.CHIP_LARGE)
            }
        }

        // 종료 버튼
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
                onClick = { viewModel.showConfirmDialog() }
            )
        }

        // 종료 확인 모달
        if (showConfirm) {
            CommonAlert(
                title = "파이널 모드를 종료하시겠습니까?",
                confirmText = "종료",
                onConfirm = {
                    viewModel.stopRecording()
                    viewModel.stopAudio()
                    viewModel.hideConfirmDialog()
                    navController.navigate(NavRoutes.FinalReportLoading.route)
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
