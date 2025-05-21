package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.text.font.FontWeight
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.TextTertiary
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.theme.White

@Composable
fun CommonReportTabBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabTitles = listOf("전체", "파이널 리포트", "코칭 리포트")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 328.dp)
                .height(40.dp)
        ) {
            tabTitles.forEachIndexed { index, title ->
                val isSelected = index == selectedTabIndex

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(index) }
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = Typography.displaySmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isSelected) Action else TextTertiary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(if (isSelected) Action else Color.Transparent)
                    )
                }
            }
        }
    }
}

@Preview(name = "Narrow screen", widthDp = 320)
@Preview(name = "Standard screen", widthDp = 360)
@Preview(name = "Wide screen", widthDp = 480)
@Composable
fun CommonReportTabBarPreview() {
    SpeakoAndroidTheme {
        var selectedTab by remember { mutableIntStateOf(1) }

        CommonReportTabBar(
            selectedTabIndex = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}
