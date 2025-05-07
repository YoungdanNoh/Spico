package com.a401.spicoandroid.presentation.home.component

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
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun PracticeSection() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val screenWidth = maxWidth
        val spacing = if (screenWidth > 360.dp) 32.dp else 16.dp
        val totalWidth = 156.dp * 2 + spacing

        Row(
            modifier = Modifier.width(totalWidth),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            PracticeModeCard(
                title = "코칭 모드",
                imageRes = R.drawable.img_coaching_home,
                onClick = { /* TODO: 코칭 모드로 이동 */ }
            )
            PracticeModeCard(
                title = "파이널 모드",
                imageRes = R.drawable.img_final_home,
                onClick = { /* TODO: 파이널 모드로 이동 */ }
            )
        }
    }
}


@Composable
fun PracticeModeCard(
    title: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(width = 156.dp, height = 192.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondary)
            .clickable(onClick = onClick)
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = Typography.displayMedium,
            color = TextPrimary
        )
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "$title 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PracticeSectionPreview() {
    SpeakoAndroidTheme {
        PracticeSection()
    }
}