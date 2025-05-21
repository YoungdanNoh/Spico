package com.a401.spicoandroid.presentation.report.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.common.ui.component.InfoSection
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography

@Composable
fun RandomReportQuestionSection(question: String) {
    InfoSection(title = "랜덤스피치 질문") {
        Text(
            text = question,
            style = Typography.bodyLarge,
            color = TextPrimary
        )
    }
}
@Preview(showBackground = true)
@Composable
fun RandomReportQuestionSectionPreview() {
    SpeakoAndroidTheme {
        RandomReportQuestionSection(
            question = "기성세대와 MZ세대 갈등에 대한 본인의 생각을 말하고 해결 방안을 제시하세요"
        )
    }
}