package com.a401.spicoandroid.common.timer

import androidx.compose.runtime.*
import kotlinx.coroutines.delay

/**
 * 발표 시작 시점부터 경과된 초(seconds) 측정
 * - 30초 이상 발표했는지 판단하는 데 사용
 *
 * @param isRunning true일 때 1초마다 1씩 증가
 * @return 현재까지 경과된 초 (State<Int>)
 */

@Composable
fun rememberElapsedSeconds(
    isRunning: Boolean
): State<Int> {
    var seconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(1000L)
                seconds++
            }
        }
    }

    return rememberUpdatedState(seconds)
}
