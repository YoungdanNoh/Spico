package com.a401.spicoandroid.presentation.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun ReportQnAItem(
    question: String,
    answer: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = question, style = Typography.headlineMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = answer, style = Typography.labelSmall, color = TextPrimary)
    }
}

@Preview(showBackground = true)
@Composable
fun FinalReportScreenPreview() {
    ReportQnAItem(
        question = "Q1. 해당 프로젝트를 하면서 어려웠던 점이 무엇인가요?",
        answer = "일정 조율이 힘들었습니다."
    )
}