package com.a401.spicoandroid.presentation.finalmode.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
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
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.infrastructure.camera.FinalRecordingCameraService
import com.a401.spicoandroid.presentation.finalmode.component.AudioWaveformView
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel

@Composable
fun FinalModeVoiceScreen(
    viewModel: FinalModeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    val countdown = viewModel.countdown
    val elapsedTime = viewModel.elapsedTime
    val showConfirm = viewModel.showStopConfirm
    val waveform by viewModel.waveform.collectAsState()

    val cameraService = remember {
        FinalRecordingCameraService(context, lifecycleOwner)
    }

    // 마이크 권한 요청
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
            viewModel.startAudio() // 권한 있을 경우 바로 시작
        }
    }

    // 카메라 녹화 시작
    LaunchedEffect(Unit) {
        cameraService.startCamera {
            viewModel.startCountdownAndRecording {
                cameraService.startRecording { uri ->
                    Log.d("FinalRecording", "저장 완료: $uri")
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 실시간 파형 + 카운트다운 겹치기
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
                    .padding(bottom = 40.dp)
            ) {
                CommonTimer(timeText = elapsedTime, type = TimerType.CHIP_LARGE)
            }
        }

        // 종료 버튼
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 32.dp, end = 24.dp)
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

        // 종료 확인 팝업
        if (showConfirm) {
            CommonAlert(
                title = "파이널 모드를 종료하시겠습니까?",
                confirmText = "종료",
                onConfirm = {
                    cameraService.stopRecording()
                    viewModel.stopRecording()
                    viewModel.stopAudio()
                    viewModel.hideConfirmDialog()
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
