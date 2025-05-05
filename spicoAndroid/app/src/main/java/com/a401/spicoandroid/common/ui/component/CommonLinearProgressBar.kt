package com.a401.spicoandroid.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.Disabled
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme

data class TimeSegment(
    val startMillis: Long,
    val endMillis: Long
)

/**
 * 공통 선형 진행바 컴포넌트
 *
 * @param totalStartMillis 전체 시작 시간 (ms)
 * @param totalEndMillis 전체 종료 시간 (ms)
 * @param segments 강조할 특정 구간 리스트 (예: 리포트 분석 구간)
 * @param progress 재생 퍼센트 (0.0 ~ 1.0). null이면 재생 표시 없음
 * @param onSeek 진행바 클릭 시 이동 함수. null이면 클릭 불가
 */
@Composable
fun CommonLinearProgressBar(
    modifier: Modifier = Modifier,
    totalStartMillis: Long,
    totalEndMillis: Long,
    segments: List<TimeSegment> = emptyList(),
    progress: Float? = null,
    onSeek: ((Float) -> Unit)? = null
) {
    val totalDuration = (totalEndMillis - totalStartMillis).toFloat().coerceAtLeast(1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .then(
                if (onSeek != null) Modifier.pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val percent = offset.x / size.width
                        onSeek(percent.coerceIn(0f, 1f))
                    }
                } else Modifier
            )
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            // 배경: 미재생 구간
            drawRect(color = Disabled)

            // 진행된 영역 (재생 기능 있을 때)
            progress?.let {
                drawRect(
                    color = Action,
                    size = Size(width * it.coerceIn(0f, 1f), height)
                )
            }

            // 강조된 구간 (리포트 피드백 등)
            segments.forEach { segment ->
                val startFraction = (segment.startMillis - totalStartMillis) / totalDuration
                val endFraction = (segment.endMillis - totalStartMillis) / totalDuration
                val startX = width * startFraction
                val segmentWidth = width * (endFraction - startFraction)

                drawRect(
                    color = Action,
                    topLeft = Offset(startX, 0f),
                    size = Size(segmentWidth, height)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportBarPreview() {
    val startMillis = 0L
    val endMillis = 60000L // 1분

    val segments = listOf(
        TimeSegment(startMillis = 10000L, endMillis = 20000L),
        TimeSegment(startMillis = 30000L, endMillis = 35000L)
    )

    SpeakoAndroidTheme {
        CommonLinearProgressBar(
            totalStartMillis = startMillis,
            totalEndMillis = endMillis,
            segments = segments,
            modifier = Modifier.padding(16.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PlaybackBarPreview() {
    val startMillis = 0L
    val endMillis = 60000L

    SpeakoAndroidTheme {
        CommonLinearProgressBar(
            totalStartMillis = startMillis,
            totalEndMillis = endMillis,
            progress = 0.45f,
            onSeek = { /* 탭 시 실행될 코드 */ }
        )
    }
}
