package com.a401.spicoandroid.presentation.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun FinalScoreCard(
    modifier: Modifier = Modifier,
    modeType: String,
    roundCount: Int,
    score: Int,
) {
    Column(
        modifier = modifier
            .height(86.dp)
            .fillMaxWidth()
            .border(1.dp, Action, shape = RoundedCornerShape(8.dp))
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$modeType ${roundCount}회차 발표 점수",
            style = Typography.headlineMedium,
            color = Action
        )
        Text(
            text = "${score}점",
            style = Typography.bodyMedium,
            color = TextPrimary
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun FinalScoreCardPreview() {
    FinalScoreCard(
        modifier = Modifier.fillMaxWidth(),
        modeType = "파이널 모드",
        roundCount = 5,
        score = 84
    )
}

