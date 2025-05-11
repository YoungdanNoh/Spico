package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.runtime.Composable
import com.a401.spicoandroid.common.ui.component.CustomOverlayTimePicker

// 1. 준비 시간 전용 다이얼로그 (1~10분, 초 고정 00)
@Composable
fun PrepTimePickerDialog(
    initialMinute: Int,
    onDismiss: () -> Unit,
    onMinuteSelected: (Int) -> Unit
) {
    CustomOverlayTimePicker(
        initialHour = 0,
        initialMinute = initialMinute,
        initialSecond = 0,
        onDismiss = onDismiss,
        onTimeSelected = { _, m, _ ->
            onMinuteSelected(m.coerceIn(1, 10))
        }
    )
}




