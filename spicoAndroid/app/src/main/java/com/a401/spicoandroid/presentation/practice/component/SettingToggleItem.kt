package com.a401.spicoandroid.presentation.practice.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonToggle
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography

@Composable
fun SettingToggleItem(
    title: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .widthIn(max = 328.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = Typography.bodyLarge,
            color = TextPrimary
        )
        CommonToggle(
            checked = checked,
            onCheckedChange = onToggle
        )
    }
}
@Composable
@Preview(showBackground = true)
fun PreviewSettingToggleItem() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingToggleItem(
            title = "청중 여부",
            checked = true,
            onToggle = {}
        )

        SettingToggleItem(
            title = "질의응답 여부",
            checked = false,
            onToggle = {}
        )
    }
}
