package com.a401.spicoandroid.presentation.practice.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    enabled: Boolean = true,
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
                color = if (enabled) TextPrimary else TextTertiary
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier.width(212.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StepperButton(
                        iconResId = R.drawable.ic_minus_text_secondary,
                        contentDescription = "감소",
                        onClick = onDecrement,
                        enabled = enabled
                    )

                    Text(
                        text = valueText,
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        color = if (enabled) TextSecondary else TextTertiary,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )


                    StepperButton(
                        iconResId = R.drawable.ic_add_text_secondary,
                        contentDescription = "증가",
                        onClick = onIncrement,
                        enabled = enabled
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
