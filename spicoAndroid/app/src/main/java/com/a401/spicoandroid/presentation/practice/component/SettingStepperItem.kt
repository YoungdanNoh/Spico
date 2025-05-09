package com.a401.spicoandroid.presentation.practice.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun SettingStepperItem(
    title: String,
    valueText: String, // 예: "2개", "1분 00초"
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    showError: Boolean = false,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 328.dp)
            .heightIn(min = 56.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Typography.bodyLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.weight(1f)) // 오른쪽 버튼 그룹을 우측에 밀어줌

            Box(modifier = Modifier.width(200.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StepperButton(
                        iconResId = R.drawable.ic_minus_text_secondary,
                        contentDescription = "감소",
                        onClick = onDecrement
                    )

                    Text(
                        text = valueText,
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )


                    StepperButton(
                        iconResId = R.drawable.ic_add_text_secondary,
                        contentDescription = "증가",
                        onClick = onIncrement
                    )
                }
            }
        }



        // 에러 메시지
        if (showError && !errorMessage.isNullOrBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = errorMessage,
                    style = Typography.labelSmall,
                    color = Error
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF7FAF8,
    widthDp = 360,
    name = "StepperItem Preview"
)
@Composable
fun PreviewSettingStepperItem() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // 정상 질문 개수
        SettingStepperItem(
            title = "질문 개수",
            valueText = "2개",
            onIncrement = {},
            onDecrement = {},
            showError = false
        )

        // 최소값 경고
        SettingStepperItem(
            title = "질문 개수",
            valueText = "1개",
            onIncrement = {},
            onDecrement = {},
            showError = true,
            errorMessage = "질문 개수는 최소 1개입니다."
        )

        // 정상 답변 시간
        SettingStepperItem(
            title = "답변 시간",
            valueText = "1분 30초",
            onIncrement = {},
            onDecrement = {},
            showError = false
        )

        // 최대 초과 경고
        SettingStepperItem(
            title = "답변 시간",
            valueText = "3분 00초",
            onIncrement = {},
            onDecrement = {},
            showError = true,
            errorMessage = "답변 시간은 최대 3분입니다."
        )
    }
}



