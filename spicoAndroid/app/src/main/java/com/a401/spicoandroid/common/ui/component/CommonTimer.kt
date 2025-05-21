package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import com.a401.spicoandroid.common.ui.theme.*

enum class TimerType {
    CIRCLE,
    CHIP_SMALL,
    CHIP_LARGE
}

@Composable
fun CommonTimer(
    timeText: String,
    type: TimerType
) {
    when (type) {
        TimerType.CIRCLE -> Box(
            modifier = Modifier
                .size(68.dp)
                .background(color = Action, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timeText,
                color = White,
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp
            )
        }

        TimerType.CHIP_SMALL -> Box(
            modifier = Modifier
                .size(width = 68.dp, height = 20.dp)
                .background(BackgroundTertiary, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timeText,
                style = MaterialTheme.typography.headlineMedium,
                color = TextSecondary
            )
        }

        TimerType.CHIP_LARGE -> Box(
            modifier = Modifier
                .size(width = 80.dp, height = 28.dp)
                .background(BackgroundTertiary, shape = RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timeText,
                style = MaterialTheme.typography.headlineLarge,
                color = TextSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
// 원형 타이머
fun CommonTimerPreview() {
    SpeakoAndroidTheme {
        Box(modifier = Modifier.background(Color.White)) {
            CommonTimer(timeText = "3", type = TimerType.CIRCLE)
        }
    }
}

@Preview(showBackground = true)
@Composable
// 코칭 모드
fun CommonTimerChipSmallPreview() {
    SpeakoAndroidTheme {
        CommonTimer(timeText = "00:05", type = TimerType.CHIP_SMALL)
    }
}

@Preview(showBackground = true)
@Composable
// 파이널 모드
fun CommonTimerChipLargePreview() {
    SpeakoAndroidTheme {
        CommonTimer(timeText = "01:00", type = TimerType.CHIP_LARGE)
    }
}
