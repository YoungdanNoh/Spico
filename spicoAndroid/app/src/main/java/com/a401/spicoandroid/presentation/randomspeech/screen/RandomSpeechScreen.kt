package com.a401.spicoandroid.presentation.randomspeech.screen

import android.Manifest
import android.app.Activity
import android.util.Log
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.timer.rememberElapsedSeconds
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.infrastructure.audio.AudioAnalyzer
import com.a401.spicoandroid.infrastructure.audio.AudioRecorderService
import com.a401.spicoandroid.presentation.navigation.LocalNavController
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.randomspeech.component.RandomSpeechExitAlert
import com.a401.spicoandroid.presentation.randomspeech.component.countdownTimer
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechSharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

@Composable
fun RandomSpeechScreen(
    viewModel: RandomSpeechSharedViewModel,
    navController: NavController = LocalNavController.current,
    onFinish: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val question = uiState.question
    val speakMin = uiState.speakTime / 60

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity

    var showExitAlert by remember { mutableStateOf(false) }
    var showExitConfirmDialog by remember { mutableStateOf(false) }

    var prepCountdown by remember { mutableIntStateOf(3) }
    var startMainTimer by remember { mutableStateOf(false) }

    val waveform = remember { mutableStateOf(emptyList<Float>()) }
    val audioAnalyzer = remember { AudioAnalyzer() }
    val audioRecorderService = remember { AudioRecorderService(context) }

    val recordedFile = remember { mutableStateOf<File?>(null) }

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
        }
    }

    // 준비 타이머
    LaunchedEffect(Unit) {
        while (prepCountdown > 0) {
            delay(1000)
            prepCountdown--
        }
        startMainTimer = true
    }

    // 녹음 시작
    LaunchedEffect(startMainTimer) {
        if (startMainTimer) {
            audioRecorderService.start(
                scope = coroutineScope,
                onWaveformUpdate = { amps ->
                    waveform.value = amps
                },
                onComplete = { file ->
                    recordedFile.value = file
                }
            )
        }
    }

    // 제한 시간이 지나면 종료 요청
    val totalSeconds = speakMin * 60
    val remainingSeconds by countdownTimer(
        totalSeconds = totalSeconds,
        isRunning = startMainTimer,
        onFinish = {
            audioRecorderService.stop()
            val file = recordedFile.value
            if (file != null) {
                coroutineScope.launch {
                    try {
                        val transcript = WhisperApiHelper.transcribeWavFile(file)
                        Log.d("ExitFlow", "⏰ 제한시간 종료 후 STT 완료")

                        viewModel.submitScript(
                            script = transcript,
                            onSuccess = {
                                val id = viewModel.getSpeechIdForReport()
                                Log.d("ExitFlow", "⏰ 제한시간 종료 후 저장 완료, id = $id")
                                if (id != null) {
                                    navController.navigate(NavRoutes.RandomSpeechReport.withId(id))
                                }
                            },
                            onError = {
                                Log.e("ExitFlow", "❌ 제한시간 종료 후 저장 실패: ${viewModel.uiState.value.errorMessage}")
                            }
                        )
                    } catch (e: Exception) {
                        Log.e("ExitFlow", "❌ 제한시간 STT 실패: ${e.message}")
                    }
                }
            }
        }
    )

    val elapsedSeconds by rememberElapsedSeconds(isRunning = startMainTimer)

    fun handleExit() {
        audioRecorderService.stop()
        if (elapsedSeconds < 30) {
            showExitConfirmDialog = true
        } else {
            showExitAlert = true
        }
    }

    BackHandler { handleExit() }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CommonTopBar(
                    centerText = "랜덤스피치",
                    rightContent = {
                        CommonButton(
                            text = "종료",
                            size = ButtonSize.XS,
                            backgroundColor = Error,
                            borderColor = Error,
                            textColor = White,
                            disabledBackgroundColor = Error,
                            disabledBorderColor = Error,
                            disabledTextColor = White,
                            enabled = prepCountdown == 0,
                            onClick = { handleExit() },
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BrokenWhite)
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(White, shape = RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_feedback_volume),
                        contentDescription = "음성 피드백",
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                InfoSection(title = null) {
                    Text(
                        text = question,
                        style = Typography.bodyLarge,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 실시간 음성 파형
                AudioWaveformView(
                    waveform = waveform.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = String.format(
                        Locale.US,
                        "%02d:%02d",
                        remainingSeconds / 60,
                        remainingSeconds % 60
                    ),
                    style = TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 40.sp
                    ),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(24.dp))

                LargeIconCircleButton(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_microphone_white),
                            contentDescription = "마이크",
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    size = 86.dp,
                    borderWidth = 6.dp,
                    borderColor = Disabled,
                    backgroundColor = Action,
                    enabled = false,
                    onClick = { }
                )

                Spacer(modifier = Modifier.height(48.dp))
            }
            // 30초 이상 종료 다이얼로그
            if (showExitAlert) {
                RandomSpeechExitAlert(
                    onDismissRequest = { showExitAlert = false },
                    onCancel = { showExitAlert = false },
                    onConfirm = {
                        showExitAlert = false
                        viewModel.setLoading(true)
                        audioRecorderService.stop()

                        Log.d("ExitFlow", "🎤 audioRecorderService stop")

                        val file = recordedFile.value
                        if (file != null) {
                            Log.d("ExitFlow", "📁 녹음 파일 존재: ${file.name}")
                            coroutineScope.launch {
                                try {
                                    val transcript = WhisperApiHelper.transcribeWavFile(file)
                                    Log.d("ExitFlow", "🗣️ Whisper 변환 완료: $transcript")

                                    viewModel.submitScript(
                                        script = transcript,
                                        onSuccess = {
                                            val id = viewModel.getSpeechIdForReport()
                                            Log.d("ExitFlow", "✅ submitScript 성공, id = $id")
                                            if (id != null) {
                                                navController.navigate(NavRoutes.RandomSpeechReport.withId(id))
                                            }
                                        },
                                        onError = {
                                            val errorMessage = viewModel.uiState.value.errorMessage
                                            Log.d("RandomSpeech", "❌ 종료 실패: $errorMessage")
                                            // TODO: 실패 처리
                                        }
                                    )
                                } catch (e: Exception) {
                                    Log.e("RandomSpeech", "❌ Whisper STT 실패: ${e.message}")
                                    // 예외 처리 필요 (e.g. 토스트 or fallback)
                                }
                            }
                        }
                    }
                )
            }
            // 30초 미만 종료 다이얼로그
            if (showExitConfirmDialog) {
                ExitConfirmDialog(
                    onDismiss = { showExitConfirmDialog = false },
                    onCancel = { showExitConfirmDialog = false },
                    onConfirm = {
                        showExitConfirmDialog = false
                        viewModel.deleteSpeech(
                            onSuccess = {
                                navController.navigate(
                                    route = NavRoutes.RandomSpeechLanding.route,
                                    navOptions = navOptions {
                                        popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = true }
                                    }
                                )
                            },
                            onError = {
                                Log.d("RandomSpeech", "❌ 삭제 실패: ${viewModel.uiState.value.errorMessage}")
                            }
                        )
                    }
                )
            }
        }
        // 중앙 타이머
        if (prepCountdown > 0) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                CommonTimer(
                    timeText = prepCountdown.toString(),
                    type = TimerType.CIRCLE
                )
            }
        }

        if (uiState.isLoading) {
            LoadingInProgressView(
                imageRes = R.drawable.character_home_5,
                message = "리포트를 생성중이에요.\n잠시만 기다려주세요!",
                homeLinkText = "리포트 목록으로 이동",
                onHomeClick = {
                    navController.navigate(
                        route = NavRoutes.RandomSpeechList.route,
                        navOptions = navOptions {
                            popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = true }
                        }
                    )
                }
            )
        }
    }
    if (uiState.isLoading) {
        LoadingInProgressView(
            imageRes = R.drawable.character_home_5,
            message = "스크립트를 저장 중이에요.\n잠시만 기다려주세요!",
            homeLinkText = "리포트 목록으로 이동",
            onHomeClick = {
                navController.navigate(
                    route = NavRoutes.RandomSpeechList.route,
                    navOptions = navOptions {
                        popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = true }
                    }
                )
            }
        )
    }
}