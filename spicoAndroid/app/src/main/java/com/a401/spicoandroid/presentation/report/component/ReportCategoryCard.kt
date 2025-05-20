package com.a401.spicoandroid.presentation.report.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.ui.component.CommonLinearProgressBar
import com.a401.spicoandroid.ui.component.TimeSegment
import com.a401.spicoandroid.R

@Composable
fun ReportCategoryCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    iconResId: Int,
    timeRangeText: String,
    totalStartMillis: Long,
    totalEndMillis: Long,
    segments: List<TimeSegment> = emptyList(),
    progress: Float? = null,
    onSeek: ((Float) -> Unit)? = null
) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = title,
                    modifier = Modifier.size(54.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = title,
                        style = Typography.displaySmall.copy(color = Action)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = description,
                        style = Typography.titleLarge.copy(color= TextPrimary),
                        maxLines = 2
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            CommonLinearProgressBar(
                totalStartMillis = totalStartMillis,
                totalEndMillis = totalEndMillis,
                segments = segments,
                progress = progress,
                onSeek = onSeek
            )

            Spacer(modifier = Modifier.height(8.dp))

//            Text(
//                text = timeRangeText,
//                style = Typography.bodySmall,
//                color = TextTertiary
//            )
        }
}

@Preview(showBackground = true)
@Composable
fun ReportCategoryCardPreview() {
    val segments = listOf(
        TimeSegment(60000L, 90000L) // 1:00 ~ 1:30
    )

    SpeakoAndroidTheme {
        ReportCategoryCard(
            title = "발음",
            description = "특정 구간에서 발음이 뭉개져요",
            iconResId = R.drawable.img_feedback_pronunciation,
            timeRangeText = "1:00 ~ 1:30",
            totalStartMillis = 0L,
            totalEndMillis = 180000L,
            segments = segments
        )
    }
}


