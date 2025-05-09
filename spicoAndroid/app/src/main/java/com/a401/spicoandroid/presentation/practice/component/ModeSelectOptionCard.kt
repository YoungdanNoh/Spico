package com.a401.spicoandroid.presentation.practice.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ChipType
import com.a401.spicoandroid.common.ui.component.CommonChip
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun ModeSelectOptionCard(
    title: String,
    description: String,
    chips: List<String>,
    @DrawableRes imageRes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minWidth = 280.dp) // 반응형 최소 너비
            .height(188.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BackgroundPrimary)
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = Typography.displayLarge,
                    color = TextPrimary
                )
                Text(
                    text = description,
                    style = Typography.bodySmall,
                    color = TextSecondary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    chips.forEach {
                        CommonChip(text = it, type = ChipType.MODE_SELECT)
                    }
                }
            }

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(112.dp)
                    .padding(start = 12.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF, // 흰 배경
    widthDp = 328, // 가로 고정
    heightDp = 188 // 세로 고정
)
@Composable
fun PreviewModeSelectOptionCard() {
    SpeakoAndroidTheme {
        ModeSelectOptionCard(
            title = "코칭모드",
            description = "발표 음성을 분석하여\n실시간 AI 피드백을 받을 수 있습니다.",
            chips = listOf("# 대본있음", "# 음성녹음"),
            imageRes = R.drawable.img_coaching_practice,
            onClick = {}
        )
    }
}


