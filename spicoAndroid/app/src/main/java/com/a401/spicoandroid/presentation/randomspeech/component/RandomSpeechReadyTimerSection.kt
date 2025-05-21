package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.a401.spicoandroid.common.ui.theme.*
import java.util.Locale

@Composable
fun RandomSpeechReadyTimerSection(
    prepMin: Int,
    onFinish: () -> Unit = {}
) {
    val totalSecondsState = countdownTimer(prepMin * 60, onFinish = onFinish)
    val totalSeconds = totalSecondsState.value

    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    Column {
        Text("남은시간", style = Typography.bodyLarge, color = TextSecondary)
        Text(
            text = String.format(Locale.KOREA, "%02d분 %02d초", minutes, seconds),
            style = Typography.displayLarge.copy(fontSize = 32.sp, textAlign = TextAlign.Start),
            color = Action
        )
        Text("발표를 준비하세요.", style = Typography.bodyLarge, color = TextTertiary)
    }
}
