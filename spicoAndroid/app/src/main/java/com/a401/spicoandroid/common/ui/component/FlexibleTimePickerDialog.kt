package com.a401.spicoandroid.common.ui.component

import androidx.compose.runtime.Composable
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.TextTertiary

@Composable
fun FlexibleTimePickerDialog(
    initialMinute: Int,
    initialSecond: Int,
    onDismiss: () -> Unit,
    onTimeSelected: (Int, Int, Int) -> Unit
) {
    BaseOverlayTimePicker(
        minuteRange = (0..30).toList(),
        secondRange = (0..59).toList(),
        initialMinute = initialMinute,
        initialSecond = initialSecond,
        validate = { minute, second ->
            val totalSeconds = minute * 60 + second
            when {
                totalSeconds < 30 -> "발표시간은 최소 30초입니다."
                totalSeconds > 1800 -> "발표시간은 최대 30분입니다."
                else -> null
            }
        },
        guideText = "발표시간은 30초 ~ 30분입니다.",
        onDismiss = onDismiss,
        onTimeSelected = onTimeSelected,
        secondColor = { selected, current ->
            if (selected == current) TextPrimary else TextTertiary
        }
    )
}

