package com.a401.spicoandroid.presentation.project.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun ProjectInfoHeader(
    title: String,
    time: String,
    onScriptClick: () -> Unit = {}
) {
    Column {
        Text(
            text = title,
            style = Typography.displayMedium,
            color = TextPrimary,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonButton(
                text = "대본 보기",
                size = ButtonSize.MD,
                backgroundColor = Action,
                textColor = White,
                modifier = Modifier.weight(1f),
                onClick = onScriptClick
            )

            CommonButton(
                text = time,
                size = ButtonSize.MD,
                backgroundColor = White,
                textColor = TextPrimary,
                borderColor = White,
                modifier = Modifier.weight(1f),
                enabled = false,
                onClick = {}
            )
        }
    }
}
