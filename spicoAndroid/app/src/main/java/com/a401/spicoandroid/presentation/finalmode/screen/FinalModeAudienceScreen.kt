package com.a401.spicoandroid.presentation.finalmode.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a401.spicoandroid.infrastructure.camera.FinalRecordingCameraService
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonAlert
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTimer
import com.a401.spicoandroid.common.ui.component.TimerType
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun FinalModeAudienceScreen(
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

    LaunchedEffect(Unit) {
        cameraService.startCamera {
            viewModel.startCountdownAndRecording {
                cameraService.startRecording { uri ->
                    Log.d("FinalRecording", "저장 완료: $uri")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.character_home_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (countdown >= 0) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                CommonTimer(timeText = countdown.toString(), type = TimerType.CIRCLE)
            }
        }

        if (countdown < 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
            ) {
                CommonTimer(timeText = elapsedTime, type = TimerType.CHIP_LARGE)
            }
        }

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