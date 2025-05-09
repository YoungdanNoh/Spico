package com.a401.spicoandroid.presentation.practice.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.BackgroundSecondary
import com.a401.spicoandroid.common.ui.theme.TextSecondary
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme

/**
 * - / + 스텝 버튼 (52 x 32)
 * 배경: BackgroundSecondary
 * 아이콘: TextSecondary 색상 아이콘
 */
@Composable
fun StepperButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .size(width = 52.dp, height = 32.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = BackgroundSecondary)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStepperButton() {
    SpeakoAndroidTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            StepperButton(
                iconResId = R.drawable.ic_minus_text_secondary,
                contentDescription = "감소",
                onClick = {}
            )
            StepperButton(
                iconResId = R.drawable.ic_add_text_secondary,
                contentDescription = "증가",
                onClick = {}
            )
        }
    }
}