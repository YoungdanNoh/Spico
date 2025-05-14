package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.timer.rememberElapsedSeconds
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.LocalNavController
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.randomspeech.component.RandomSpeechExitAlert
import com.a401.spicoandroid.presentation.randomspeech.component.countdownTimer
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechSharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun RandomSpeechScreen(
    navController: NavController = LocalNavController.current,
    question: String,
    speakMin: Int,
    onFinish: () -> Unit = {}
) {
    val viewModel: RandomSpeechSharedViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    var showExitAlert by remember { mutableStateOf(false) }           // 30초 이상 종료 시 확인
    var showExitConfirmDialog by remember { mutableStateOf(false) }   // 30초 미만 종료 시 안내

    var prepCountdown by remember { mutableIntStateOf(3) }
    var startMainTimer by remember { mutableStateOf(false) }

    // 준비 카운트다운
    LaunchedEffect(Unit) {
        while (prepCountdown > 0) {
            delay(1000)
            prepCountdown--
        }
        startMainTimer = true
    }

    // 발표 타이머
    val totalSeconds = speakMin * 60
    val remainingSeconds by countdownTimer(
        totalSeconds = totalSeconds,
        onFinish = onFinish,
        isRunning = startMainTimer
    )
    val elapsedSeconds by rememberElapsedSeconds(isRunning = startMainTimer)

    // 종료 처리 함수
    fun handleExit() {
        if (elapsedSeconds < 30) {
            showExitConfirmDialog = true
        } else {
            showExitAlert = true
        }
    }

    BackHandler {
        handleExit()
    }

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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Action.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("음성 주파수", color = TextSecondary)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = String.format(Locale.US, "%02d:%02d", remainingSeconds / 60, remainingSeconds % 60),
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

            // 30초 이상 → 리포트로 이동
            if (showExitAlert) {
                RandomSpeechExitAlert(
                    onDismissRequest = { showExitAlert = false },
                    onCancel = { showExitAlert = false },
                    onConfirm = {
                        showExitAlert = false
                        val id = viewModel.getSpeechIdForReport()
                        if (id != null) {
                            navController.navigate(NavRoutes.RandomSpeechReport.withId(id))
                        } else {
                            // 예외 처리
                        }
                    }
                )
            }

            // 30초 미만 → 리포트 생성 안됨
            if (showExitConfirmDialog) {
                ExitConfirmDialog(
                    onDismiss = { showExitConfirmDialog = false },
                    onCancel = { showExitConfirmDialog = false },
                    onConfirm = {
                        showExitConfirmDialog = false
                        navController.navigate(
                            route = NavRoutes.RandomSpeechLanding.route,
                            navOptions = navOptions {
                                popUpTo(NavRoutes.RandomSpeechLanding.route) {
                                    inclusive = true
                                }
                            }
                        )
                    }
                )
            }
        }

        // 중앙 준비 타이머
        if (prepCountdown > 0) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                CommonTimer(
                    timeText = prepCountdown.toString(),
                    type = TimerType.CIRCLE
                )
            }
        }
    }
}
