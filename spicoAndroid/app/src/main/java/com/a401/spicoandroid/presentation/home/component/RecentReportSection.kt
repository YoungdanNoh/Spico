package com.a401.spicoandroid.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.ui.theme.dropShadow1

data class PracticeReport(
    val practiceId: Int,
    val reportsId: Int,
    val practiceName: String,
    val projectName: String
)

@Composable
fun RecentReportSection(
    reportList: List<PracticeReport> = listOf(
        PracticeReport(101, 12343, "자율 프로젝트 결선 코칭 1트", "자율 프로젝트"),
        PracticeReport(102, 12344, "자율 프로젝트 결선 코칭 2트", "특화 프로젝트"),
        PracticeReport(103, 12345, "자율 프로젝트 결선 파이널 1트", "자율 프로젝트")
    )
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrokenWhite)
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
                title = report.practiceName,
                description = report.projectName
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
