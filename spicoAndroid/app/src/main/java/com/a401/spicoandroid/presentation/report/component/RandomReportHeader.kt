package com.a401.spicoandroid.presentation.report.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonChip
import com.a401.spicoandroid.common.ui.component.ChipType
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.utils.getTopicIconRes

@Composable
fun RandomReportHeader(title: String, topic: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(id = getTopicIconRes(topic)),
            contentDescription = "주제 아이콘",
            modifier = Modifier.size(60.dp),
            tint = Color.Unspecified
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(title, style = Typography.displayMedium, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            CommonChip(text = "랜덤 스피치", type = ChipType.REPORT_ACTION)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RandomReportHeaderPreview() {
    SpeakoAndroidTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            RandomReportHeader(
                title = "MZ는 개복치다아아아아아아아아아아아아",
                topic = "economy" // getTopicIconRes()에 대응하는 키
            )
        }
    }
}