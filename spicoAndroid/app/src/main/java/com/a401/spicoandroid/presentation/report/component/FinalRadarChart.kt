package com.a401.spicoandroid.presentation.report.component

import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import androidx.compose.ui.graphics.toArgb
import com.a401.spicoandroid.common.ui.theme.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun FinalRadarChart(
    modifier: Modifier = Modifier,
    labels: List<String> = listOf("발음", "속도", "성량", "휴지", "대본"),
    scores: List<Float> = listOf(80f, 70f, 90f, 85f, 75f)
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            RadarChart(context).apply {

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                setExtraOffsets(24f, 24f, 24f, 24f)

                // 애니메이션
                animateXY(1000, 1000, Easing.EaseInOutQuad)

                // 데이터 세팅
                val entries = scores.map { RadarEntry(it) }
                val dataSet = RadarDataSet(entries, "발표 분석 결과").apply {
                    color = Hover.toArgb()
                    fillColor = Action.toArgb()
                    fillAlpha = 120
                    setDrawFilled(true)
                    lineWidth = 2f
                    valueTextColor = TextPrimary.toArgb()
                    valueTextSize = 12f
                    setDrawValues(true)

                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    }
                }

                data = RadarData(dataSet)

                // 웹 스타일
                webLineWidth = 1.5f
                webColor = Disabled.toArgb()
                webLineWidthInner = 1f
                webColorInner = Action.toArgb()
                webAlpha = 200

                // X축
                xAxis.apply {
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return labels.getOrNull(value.toInt() % labels.size) ?: ""
                        }
                    }
                    textSize = 16f
                    textColor = TextPrimary.toArgb()
                    position = XAxis.XAxisPosition.BOTTOM
                }

                // Y축
                yAxis.apply {
                    axisMinimum = 0f
                    axisMaximum = 100f
                    setDrawLabels(false)
                }

                description.isEnabled = false
                legend.isEnabled = false

                invalidate()
            }
        }
    )
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun FinalRadarChartPreview() {
    FinalRadarChart()
}