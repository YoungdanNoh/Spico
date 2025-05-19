package com.a401.spicoandroid.presentation.finalmode.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.timer.rememberElapsedSeconds
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.infrastructure.camera.FinalRecordingCameraService
import com.a401.spicoandroid.presentation.finalmode.component.VideoBackgroundPlayer
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun FinalModeAudienceScreen(
    navController: NavController,
    parentNavController: NavController,
    projectId: Int,
    practiceId: Int,
    viewModel: FinalModeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val countdown = viewModel.countdown
    val elapsedTime = viewModel.elapsedTime
    val elapsedSeconds = rememberElapsedSeconds(isRunning = countdown < 0)

    val cameraService = remember {
        FinalRecordingCameraService(context, lifecycleOwner)
    }

    val navigateToProjectList = remember { mutableStateOf(false) }

    LaunchedEffect(navigateToProjectList.value) {
        if (navigateToProjectList.value) {
            Log.d("FinalFlow", "➡️ 이동: ProjectList")
            navController.navigate(NavRoutes.ProjectList.route) {
                popUpTo(NavRoutes.ProjectList.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            cameraService.startCamera {
                viewModel.startCountdownAndRecording {
                    cameraService.startRecording { uri ->
                        Log.d("FinalRecording", "저장 완료: $uri")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FinalFlow", "❌ 카메라 실행 중 예외 발생", e)
        }
    }

    // 뒤로 가기 막기
    BackHandler(enabled = true) {
        if (countdown < 0) {
            viewModel.checkElapsedAndShowDialog(elapsedSeconds.value)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VideoBackgroundPlayer(
            context = context,
            videoResId = R.raw.final_normal,
            modifier = Modifier.fillMaxSize()
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
                onClick = {
                    viewModel.checkElapsedAndShowDialog(elapsedSeconds.value)
                },
                enabled = countdown < 0
            )
        }

        if (viewModel.showNormalExitDialog) {
            CommonAlert(
                title = "파이널 모드를\n종료하시겠습니까?",
                confirmText = "종료",
                onConfirm = {
                    viewModel.hideAllDialogs()
                    viewModel.stopRecording()
                    viewModel.stopAudio()

                    Log.d("FinalFlow", "🛑 녹화 종료. projectId=$projectId, practiceId=$practiceId")

                    // 종료 버튼 → 알럿 확인 이후
                    if (viewModel.getHasQnA()) {
                        navController.navigate(
                            NavRoutes.FinalModeLoading.withArgs(
                                FinalModeLoadingType.QUESTION, projectId, practiceId
                            )
                        )
                    } else {
                        navController.navigate(
                            NavRoutes.FinalModeLoading.withArgs(
                                FinalModeLoadingType.REPORT, projectId, practiceId
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
