package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.LineTertiary
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme

@Composable
fun CommonCircularProgressBar(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    totalCount: Int = 5
) {
    val dotSize = 8.dp
    val spacing = 4.dp
    val barWidth = (dotSize * totalCount) + (spacing * (totalCount - 1)) // = 60.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.width(barWidth),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalCount) { index ->
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(
                            color = if (index == selectedIndex) Action else LineTertiary
                        )
                        .clickable { onSelect(index) }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun CircularProgressBarPreview() {
    SpeakoAndroidTheme {
        CommonCircularProgressBar(
            selectedIndex = 3,
            onSelect = {}
        )
    }
}
