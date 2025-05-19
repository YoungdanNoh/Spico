package com.a401.spicoandroid.presentation.finalmode.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.common.timer.rememberElapsedSeconds
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.infrastructure.camera.FinalRecordingCameraService
import com.a401.spicoandroid.common.ui.component.AudioWaveformView
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun FinalModeVoiceScreen(
    navController: NavController,
    parentNavController: NavController,
    projectId: Int,
    practiceId: Int,
    viewModel: FinalModeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    val countdown = viewModel.countdown
    val elapsedTime = viewModel.elapsedTime
    val waveform by viewModel.waveform.collectAsState()
    val elapsedSeconds = rememberElapsedSeconds(isRunning = countdown < 0)

    val cameraService = remember {
        FinalRecordingCameraService(context, lifecycleOwner)
    }

    // 🎙 마이크 권한 요청 및 오디오 분석 시작
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity ?: return@LaunchedEffect,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1001
            )
        } else {
            viewModel.startAudio()
        }
    }

    // 🎥 카메라 녹화 시작
    LaunchedEffect(Unit) {
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
        if (countdown < 0) {
            viewModel.checkElapsedAndShowDialog(elapsedSeconds.value)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 실시간 파형 + 카운트다운
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
                .height(160.dp)
        ) {
            AudioWaveformView(waveform = waveform, modifier = Modifier.fillMaxSize())

            if (countdown >= 0) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    CommonTimer(timeText = countdown.toString(), type = TimerType.CIRCLE)
                }
            }
        }

        // 경과 시간
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
                onClick = {
                    viewModel.checkElapsedAndShowDialog(elapsedSeconds.value)
                },
                enabled = countdown < 0
            )
        }

        // 30초 이상 종료 다이얼로그
        if (viewModel.showNormalExitDialog) {
            CommonAlert(
                title = "파이널 모드를\n종료하시겠습니까?",
                confirmText = "종료",
                onConfirm = {
                    viewModel.hideAllDialogs()
                    viewModel.stopRecording()
                    viewModel.stopAudio()

                    Log.d("FinalFlow", "🛑 녹화 종료. projectId=$projectId, practiceId=$practiceId")

                    if (viewModel.getHasQnA()) {
                        navController.navigate(
                            NavRoutes.FinalModeLoading.withArgs(
                                FinalModeLoadingType.QUESTION,
                                projectId,
                                practiceId
                            )
                        )
                    } else {
                        parentNavController.navigate(
                            NavRoutes.FinalModeLoading.withArgs(
                                FinalModeLoadingType.REPORT,
                                projectId,
                                practiceId
                            )
                        )
                    }
                },
                onCancel = { viewModel.hideAllDialogs() },
                onDismissRequest = { viewModel.hideAllDialogs() },
                confirmTextColor = White,
                confirmBackgroundColor = Error,
                confirmBorderColor = Error,
                cancelText = "취소",
                cancelTextColor = TextTertiary,
                cancelBackgroundColor = BackgroundSecondary,
                cancelBorderColor = BackgroundSecondary,
                borderColor = White
            )
        }

        // 30초 미만 종료 다이얼로그
        if (viewModel.showEarlyExitDialog) {
            CommonAlert(
                title = "30초 미만의 발표는\n리포트가 제공되지 않아요.\n종료하시겠어요?",
                confirmText = "종료",
                onConfirm = {
                    viewModel.hideAllDialogs()
                    viewModel.stopRecording()
                    viewModel.stopAudio()
                    cameraService.stopRecording {
                        parentNavController.navigate(NavRoutes.ProjectList.route) {
                            popUpTo(NavRoutes.ProjectList.route) { inclusive = true }
                        }
                    }
                },
                onCancel = { viewModel.hideAllDialogs() },
                onDismissRequest = { viewModel.hideAllDialogs() },
                confirmTextColor = White,
                confirmBackgroundColor = Error,
                confirmBorderColor = Error,
                cancelText = "취소",
                cancelTextColor = TextTertiary,
                cancelBackgroundColor = BackgroundSecondary,
                cancelBorderColor = BackgroundSecondary,
                borderColor = White
            )
        }
    }
}
