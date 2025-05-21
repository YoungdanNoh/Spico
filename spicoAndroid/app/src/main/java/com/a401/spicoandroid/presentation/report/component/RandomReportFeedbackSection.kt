package com.a401.spicoandroid.presentation.report.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.common.ui.component.InfoSection
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography

@Composable
fun RandomReportFeedbackSection(feedback: String) {
    InfoSection(title = "피드백") {
        Text(
            text = feedback,
            style = Typography.bodyLarge,
            color = TextPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RandomReportFeedbackSectionPreview() {
    SpeakoAndroidTheme {
        RandomReportFeedbackSection(
            feedback = "전반적으로 좋아요. 근거도 있고, 기승전결도 좋습니다. 발표를 아주 잘하시네요!"
        )
    }
}
