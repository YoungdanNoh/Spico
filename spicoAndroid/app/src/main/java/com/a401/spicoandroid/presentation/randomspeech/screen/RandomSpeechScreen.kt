package com.a401.spicoandroid.presentation.randomspeech.screen

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
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.randomspeech.component.CountdownTimer
import com.a401.spicoandroid.presentation.randomspeech.component.RandomSpeechExitAlert
import java.util.Locale

@Composable
fun RandomSpeechScreen(
    question: String,
    speakMin: Int,
    onFinish: () -> Unit = {}
) {
    var showExitAlert by remember { mutableStateOf(false) }

    val totalSeconds = speakMin * 60
    val remainingSeconds by CountdownTimer(totalSeconds = totalSeconds, onFinish = onFinish)

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

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
                        onClick = { showExitAlert = true }
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
                text = String.format(Locale.US, "%02d:%02d", minutes, seconds),
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
                onClick = { /* TODO: 녹음 시작 or 토글 */ }
            )

            Spacer(modifier = Modifier.height(48.dp))
        }

        if (showExitAlert) {
            RandomSpeechExitAlert(
                onDismissRequest = { showExitAlert = false },
                onCancel = { showExitAlert = false },
                onConfirm = {
                    showExitAlert = false
                    onFinish()
                }
            )
        }
    }
}

