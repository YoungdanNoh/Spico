package com.a401.spicoandroid.common.ui.component


import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*

@SuppressLint("RememberReturnType")
@Composable
fun FinalAudioWaveformView(
    waveform: List<Float>,
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        val centerY = size.height / 2
        val barWidth = size.width / waveform.size.coerceAtLeast(1)

        waveform.forEachIndexed { index, amp ->
            val scaledAmp = (amp).coerceIn(0.05f, 1f)
            val barHeight = scaledAmp * size.height
            drawLine(
                color = Action,
                start = Offset(x = index * barWidth, y = centerY - barHeight / 2),
                end = Offset(x = index * barWidth, y = centerY + barHeight / 2),
                strokeWidth = 4f
            )
        }
    }
}

