package com.a401.spicoandroid.presentation.coachingmode.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.bottomsheet.ScriptBottomSheet
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.infrastructure.speech.GoogleStt
import com.a401.spicoandroid.presentation.coachingmode.component.CoachingFeedbackPanel
import com.a401.spicoandroid.presentation.coachingmode.viewmodel.CoachingModeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectScriptViewModel
import com.a401.spicoandroid.presentation.report.viewmodel.CoachingReportViewModel

@Composable
fun CoachingModeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    coachingModeViewModel: CoachingModeViewModel = hiltViewModel(),
    scriptViewModel: ProjectScriptViewModel = hiltViewModel(),
    projectId: Int,
    practiceId: Int
) {
    BackHandler(enabled = true) {
        coachingModeViewModel.showConfirmDialog()
    }

    val context = LocalContext.current
    val activity = context as? Activity

    val state by coachingModeViewModel.coachingState.collectAsState()
    val scriptState by scriptViewModel.scriptState.collectAsState()

    val coachingReportViewModel: CoachingReportViewModel = hiltViewModel()

    val showScriptSheet = remember { mutableStateOf(false) }
    var permissionGranted by remember { mutableStateOf(false) }

    val showEarlyExitConfirm = remember { mutableStateOf(false) }

    // STT 설정
    val googleStt = remember {
        GoogleStt(
            context = context,
            onResult = { Log.d("STT", "결과: $it") },
            onError = { Log.e("STT", it) }
        ).apply {
            setOnWaveformUpdate { coachingModeViewModel.pushWaveformValue(it) }
            setOnVolumeFeedback { coachingModeViewModel.updateVolumeFeedback(it) }
            setOnPartialResult { partialText ->
                coachingModeViewModel.updateSpokenText(partialText)
            }
        }
    }

    // 권한 요청
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

    LaunchedEffect(scriptState) {
        val texts = scriptState.paragraphs.map { it.text }
        coachingModeViewModel.initializeScript(texts)
    }

    LaunchedEffect(permissionGranted) {
        Log.d("Permission", "녹음 권한 granted? $permissionGranted")
        if (permissionGranted) {
            coachingModeViewModel.startCountdownAndRecording {
                googleStt.start()
            }
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            Log.d("PostResult", "📦 Navigation 이동 시작")
            navController.navigate(
                route = NavRoutes.CoachingReport.withArgs(projectId = projectId, practiceId = practiceId),
                navOptions = navOptions {
                    popUpTo(NavRoutes.CoachingMode.route) { inclusive = true }
                }
            )
        }
    }

    val isCountdown = state.countdown >= 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrokenWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            CoachingFeedbackPanel(
                modifier = Modifier.fillMaxWidth(),
                characterPainter = painterResource(id = R.drawable.character_coaching),
                latestFeedback = state.volumeFeedback ?: ""
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clipToBounds()
            ) {
                AudioWaveformView(
                    waveform = state.waveform,
                    modifier = Modifier.fillMaxSize()
                )
                if (isCountdown) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        CommonTimer(timeText = state.countdown.toString(), type = TimerType.CIRCLE)
                    }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonTimer(timeText = state.elapsedTime, type = TimerType.CHIP_LARGE)
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
                            painter = painterResource(id = R.drawable.ic_microphone_white),
                            contentDescription = "Record",
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    borderColor = Disabled,
                    backgroundColor = Action,
                    enabled = false,
                    onClick = {}
                )

                CommonButton(
                    text = "종료",
                    backgroundColor = Error,
                    borderColor = Error,
                    textColor = White,
                    size = ButtonSize.SM,
                    onClick = { coachingModeViewModel.showConfirmDialog() }
                )
            }
        }

        if (state.showStopConfirm) {
            val canStop = coachingModeViewModel.stopRecording()
            if (!canStop){
                CommonAlert(
                    title = "지금 종료하면 리포트가\n제공되지 않습니다.",
                    confirmText = "종료",
                    onConfirm = {
                        googleStt.stop()
                        coachingReportViewModel.deleteReport(
                            projectId = projectId,
                            practiceId = practiceId,
                            onSuccess = {
                                navController.navigate(NavRoutes.ProjectDetail.withId(projectId)) {
                                    popUpTo(NavRoutes.ProjectList.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            onError = {
                                Log.d("Coaching", "❌ 삭제 실패")
                            }
                        )
                        coachingModeViewModel.hideConfirmDialog()
                    },
                    cancelText = "취소",
                    onCancel = { coachingModeViewModel.hideConfirmDialog() },
                    onDismissRequest = { coachingModeViewModel.hideConfirmDialog() },
                    confirmTextColor = White,
                    confirmBackgroundColor = Error,
                    confirmBorderColor = Error,
                    cancelTextColor = TextTertiary,
                    cancelBackgroundColor = BackgroundSecondary,
                    cancelBorderColor = BackgroundSecondary,
                    borderColor = White,
                )
            } else {
                CommonAlert(
                    title = "코칭 모드를 종료하시겠습니까?",
                    confirmText = "종료",
                    onConfirm = {
                        googleStt.stop()
                        coachingModeViewModel.calculateAndStoreVolumeScore(
                            records = googleStt.getVolumeRecordList()
                        )
                        coachingModeViewModel.postResult(projectId = projectId, practiceId = practiceId)
                        coachingModeViewModel.hideConfirmDialog()
                    },
                    cancelText = "취소",
                    onCancel = { coachingModeViewModel.hideConfirmDialog() },
                    onDismissRequest = { coachingModeViewModel.hideConfirmDialog() },
                    confirmTextColor = White,
                    confirmBackgroundColor = Error,
                    confirmBorderColor = Error,
                    cancelTextColor = TextTertiary,
                    cancelBackgroundColor = BackgroundSecondary,
                    cancelBorderColor = BackgroundSecondary,
                    borderColor = White,
                )
            }
        }

        if (showScriptSheet.value) {
            ScriptBottomSheet(
                scripts = scriptState.paragraphs.map { it.text },
                highlightedIndex = state.currentParagraphIndex,
                onScriptClick = { showScriptSheet.value = false },
                onDismissRequest = { showScriptSheet.value = false }
            )
        }
    }
}

