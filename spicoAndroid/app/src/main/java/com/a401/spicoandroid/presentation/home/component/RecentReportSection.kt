package com.a401.spicoandroid.presentation.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.ui.theme.dropShadow1

data class Report(
    val title: String,
    val description: String
)

@Composable
fun RecentReportSection(
    reportList: List<Report> = listOf(
        Report("코칭 모드 3회차 리포트", "자율 프로젝트"),
        Report("파이널 모드 2회차 리포트", "자율 프로젝트"),
        Report("코칭 모드 1회차 리포트", "공통 프로젝트")
    )
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "최근 연습 리포트",
            style = Typography.displaySmall,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        reportList.forEachIndexed { index, report ->
            CommonList(
                modifier = Modifier.dropShadow1(),
                title = report.title,
                description = report.description,
            )
            if (index < reportList.lastIndex) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun RecentReportSectionPreview() {
    SpeakoAndroidTheme {
        RecentReportSection()
    }
}
