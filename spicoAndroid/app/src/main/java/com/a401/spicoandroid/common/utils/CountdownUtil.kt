package com.a401.spicoandroid.common.utils

import kotlinx.coroutines.delay

suspend fun startCountdown(
    seconds: Int = 3,
    onTick: (Int) -> Unit,
    onFinish: () -> Unit
) {
    for (i in seconds downTo 1) {
        onTick(i)
        delay(1000)
    }
    onTick(0)
    delay(1000)
    onFinish()
}
