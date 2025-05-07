package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    leftContent: (@Composable () -> Unit)? = null,
    centerText: String,
    rightContent: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                leftContent?.invoke()
            }

            Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.Center) {
                Text(
                    text = centerText,
                    style = Typography.displayMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                rightContent?.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTopBarPreview() {
    Column {
        // 1. 텍스트
        CommonTopBar(
            centerText = "텍스트"
        )

        // 2. 아이콘 + 텍스트
        CommonTopBar(
            centerText = "텍스트",
            leftContent = {
                IconButton(
                    iconResId = R.drawable.ic_arrow_left_black,
                    contentDescription = "뒤로가기",
                    onClick = {}
                )
            }
        )

        // 3. 텍스트 + 아이콘
        CommonTopBar(
            centerText = "텍스트",
            rightContent = {
                IconButton(
                    iconResId = R.drawable.ic_arrow_right_balck,
                    contentDescription = "다음",
                    onClick = {}
                )
            }
        )

        // 4. 아이콘 + 텍스트 + 아이콘
        CommonTopBar(
            centerText = "텍스트",
            leftContent = {
                IconButton(
                    iconResId = R.drawable.ic_arrow_left_black,
                    contentDescription = "뒤로가기",
                    onClick = {}
                )
            },
            rightContent = {
                IconButton(
                    iconResId = R.drawable.ic_arrow_right_balck,
                    contentDescription = "다음",
                    onClick = {}
                )
            }
        )
    }
}
