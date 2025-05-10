package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun InfoSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = Typography.headlineLarge,
            color = TextPrimary
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .dropShadow1(cornerRadius = 8.dp)
                .background(White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = content
        )
    }
}
@Preview(showBackground = true)
@Composable
fun InfoSectionPreview() {
    SpeakoAndroidTheme {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            InfoSection("랜덤스피치 질문") {
                Text(
                    text = "MZ세대는 기성세대와 어떤 점에서 다르다고 생각하나요?\n또, 그 차이를 어떻게 이해하고 조화롭게 해결할 수 있을까요?",
                    style = Typography.bodyLarge,
                    color = TextPrimary
                )
            }

            InfoSection("관련기사") {
                Text("국내 주식형펀드서 사흘째 자금 순유출", style = Typography.displaySmall, color = TextPrimary)
                Text(
                    "국내 주식형 펀드에서 사흘째 자금이 빠져나갔다. 투자심리가 위축되고 있다는 신호로 해석된다.",
                    style = Typography.bodyLarge,
                    color = TextSecondary
                )
                CommonButton(
                    text = "기사 원문 확인하기",
                    size = ButtonSize.LG,
                    backgroundColor = White,
                    borderColor = Action,
                    textColor = Action,
                    onClick = {}
                )
            }

            InfoSection("피드백") {
                Text(
                    text = "전반적으로 좋아요. 근거도 있고, 기승전결도 좋습니다. 발표를 아주 잘하시네요!",
                    style = Typography.bodyLarge,
                    color = TextPrimary
                )
            }
        }
    }
}

