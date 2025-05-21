package com.a401.spicoandroid.presentation.report.component

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.InfoSection
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.openExternalLink

@Composable
fun RandomReportNewsSection(
    title: String,
    summary: String,
    url: String,
    context: Context
) {
    val maxLength = 300
    val trimmedSummary = if (summary.length > maxLength) {
        summary.substring(0, maxLength) + "..."
    } else {
        summary
    }

    InfoSection(title = "관련기사") {
        Text(title, style = Typography.displaySmall, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(trimmedSummary, style = Typography.titleMedium, color = TextTertiary)
        Spacer(modifier = Modifier.height(12.dp))
        CommonButton(
            text = "기사 원문 확인하기",
            size = ButtonSize.LG,
            backgroundColor = White,
            borderColor = Action,
            textColor = Action,
            onClick = { openExternalLink(context, url) }
        )
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun RandomReportNewsSectionPreview() {
    SpeakoAndroidTheme {
        // 프리뷰에서는 context 대체로 사용 불가하므로 더미 버튼만 표시
        InfoSection(title = "관련기사") {
            Text("국내 주식형펀드서 사흘째 자금 순유출", style = Typography.displaySmall, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text("국내 주식형 펀드에서 사흘째 자금이 빠져나갔다. ...", style = Typography.titleMedium, color = TextTertiary)
            Spacer(modifier = Modifier.height(12.dp))
            CommonButton(
                text = "기사 원문 확인하기",
                size = ButtonSize.LG,
                backgroundColor = White,
                borderColor = Action,
                textColor = Action,
                onClick = {}
            )
        }
    }
}
