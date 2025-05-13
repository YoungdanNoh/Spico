package com.a401.spicoandroid.presentation.coachingmode.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.bottomsheet.ScriptBottomSheet
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.coachingmode.component.CoachingFeedbackPanel
import com.a401.spicoandroid.presentation.coachingmode.viewmodel.CoachingModeViewModel
import com.a401.spicoandroid.presentation.coachingmode.viewmodel.RecordingState
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectScriptViewModel

@Composable
fun CoachingModeScreen(
    navController: NavController,
    coachingModeviewModel: CoachingModeViewModel = hiltViewModel(),
    scriptViewModel: ProjectScriptViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val countdown = coachingModeviewModel.countdown
    val elapsedTime = coachingModeviewModel.elapsedTime
    val showConfirm = coachingModeviewModel.showStopConfirm
    val recordingState = coachingModeviewModel.recordingState

    val waveform by coachingModeviewModel.waveform.collectAsState()
    val scriptState by scriptViewModel.scriptState.collectAsState()

    val showScriptSheet = remember { mutableStateOf(false) }

    var permissionGranted by remember { mutableStateOf(false) }

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
            permissionGranted = true
        }
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            coachingModeviewModel.startCountdownAndRecording()
        }
    }

    val isCountdown = countdown >= 0
    val isPaused = recordingState == RecordingState.PAUSED
    val isRecording = recordingState == RecordingState.RECORDING

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrokenWhite)
    ) {

        CoachingFeedbackPanel(
            modifier = Modifier.padding(16.dp),
            characterPainter = painterResource(id = R.drawable.character_coaching),
            latestFeedback = "목소리를 더 키워봐요!"
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 16.dp)
                .height(200.dp)
        ) {
            AudioWaveformView(waveform = waveform, modifier = Modifier.fillMaxSize())
            if (isCountdown) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    CommonTimer(timeText = countdown.toString(), type = TimerType.CIRCLE)
                }
            }
        }

        Column(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .background(White)
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonTimer(timeText = elapsedTime, type = TimerType.CHIP_LARGE)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(45.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommonButton(
                    text = "대본",
                    backgroundColor = White,
                    borderColor = Action,
                    textColor = Action,
                    size = ButtonSize.SM,
                    onClick = { showScriptSheet.value = true }
                )

                LargeIconCircleButton(
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = when {
                                    isCountdown || isPaused -> R.drawable.ic_record_white
                                    isRecording -> R.drawable.ic_pause_white
                                    else -> R.drawable.ic_record_white
                                }
                            ),
                            contentDescription = "Record",
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    borderColor = when {
                        isCountdown || isPaused -> Error
                        isRecording -> Disabled
                        else -> Disabled
                    },
                    backgroundColor = when {
                        isCountdown || isPaused -> ErrorHover
                        isRecording -> Action
                        else -> Disabled
                    },
                    enabled = !isCountdown,
                    onClick = { if (!isCountdown) coachingModeviewModel.toggleRecording() }
                )

                CommonButton(
                    text = "종료",
                    backgroundColor = Error,
                    borderColor = Error,
                    textColor = White,
                    size = ButtonSize.SM,
                    onClick = { coachingModeviewModel.showConfirmDialog() }
                )
            }
        }

        if (showConfirm) {
            CommonAlert(
                title = "코칭 모드를 종료하시겠습니까?",
                confirmText = "종료",
                onConfirm = {
                    val canStop = coachingModeviewModel.stopRecording()
                    if (!canStop) {
                        Toast.makeText(context, "녹음이 너무 짧아요! 조금 더 녹음해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate("home")
                    }
                    coachingModeviewModel.hideConfirmDialog()
                },
                cancelText = "취소",
                onCancel = { coachingModeviewModel.hideConfirmDialog() },
                onDismissRequest = { coachingModeviewModel.hideConfirmDialog() },
                confirmTextColor = White,
                confirmBackgroundColor = Error,
                confirmBorderColor = Error,
                cancelTextColor = TextTertiary,
                cancelBackgroundColor = BackgroundSecondary,
                cancelBorderColor = BackgroundSecondary,
                borderColor = White,
            )
        }

        if (showScriptSheet.value) {
            ScriptBottomSheet(
                scripts = scriptState.paragraphs.map { it.text },
                onScriptClick = { index ->
                    Log.d("Script", "선택된 스크립트 인덱스: $index")
                    showScriptSheet.value = false
                },
                onDismissRequest = {
                    showScriptSheet.value = false
                }
            )
        }
    }
}
