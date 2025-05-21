package com.a401.spicoandroid.presentation.randomspeech.component

import androidx.compose.runtime.Composable
import com.a401.spicoandroid.common.ui.component.BaseOverlayTimePicker
import com.a401.spicoandroid.common.ui.theme.TextTertiary

// 준비 시간 전용 다이얼로그 (1~10분, 초 고정 00)
@Composable
fun PrepTimePickerDialog(
    initialMinute: Int,
    onDismiss: () -> Unit,
    onMinuteSelected: (Int) -> Unit
) {
    require(initialMinute in 1..10)

    BaseOverlayTimePicker(
        minuteRange = (1..10).toList(),
        secondRange = listOf(0),
        initialMinute = initialMinute,
        initialSecond = 0,
        validate = { _, _ -> null },
        guideText = "준비시간은 분 단위 입니다.",
        onDismiss = onDismiss,
        onTimeSelected = { _, m, _ -> onMinuteSelected(m) },
        secondColor = { _, _ -> TextTertiary }
    )
}





