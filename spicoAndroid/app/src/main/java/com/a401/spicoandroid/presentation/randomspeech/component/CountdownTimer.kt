package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@Composable
fun countdownTimer(
    totalSeconds: Int,
    onFinish: () -> Unit = {},
    isRunning: Boolean = true
): State<Int> {
    val seconds = remember { mutableIntStateOf(totalSeconds) }

    LaunchedEffect(seconds.intValue, isRunning) {
        if (isRunning && seconds.intValue > 0) {
            delay(1000)
            seconds.intValue -= 1
        } else if (isRunning && seconds.intValue == 0) {
            onFinish()
        }
    }

    return seconds
}
