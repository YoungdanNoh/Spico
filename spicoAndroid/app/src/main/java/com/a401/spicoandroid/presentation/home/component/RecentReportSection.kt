package com.a401.spicoandroid.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.ui.theme.dropShadow1
import com.a401.spicoandroid.presentation.home.dummy.DummyPracticeReports

data class PracticeReport(
    val practiceId: Int,
    val reportsId: Int,
    val practiceName: String,
    val projectName: String
)

@Composable
fun RecentReportSection(
    modifier: Modifier = Modifier,
//    reportList: List<PracticeReport> = DummyPracticeReports
    reportList: List<PracticeReport> = emptyList()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BrokenWhite)
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "최근 연습 리포트",
            style = Typography.displaySmall,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (reportList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp), // 원하는 여백 조절
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "최근 연습 리포트가 없습니다.",
                    style = Typography.titleLarge,
                    color = TextTertiary
                )
            }
        } else {
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
}
@Preview(showBackground = true, widthDp = 360)
@Composable
fun RecentReportSection_EmptyPreview() {
    SpeakoAndroidTheme {
        RecentReportSection(
            reportList = emptyList(),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

