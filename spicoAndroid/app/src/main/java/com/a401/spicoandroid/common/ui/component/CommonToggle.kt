package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.TextTertiary

@Composable
fun CommonToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val switchColor = if (checked) Action else TextTertiary
    val alignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .width(56.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(100.dp)) // 완전한 pill shape
            .background(switchColor)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(24.dp)
                .background(Color.White, CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTogglePreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        // OFF 상태
        CommonToggle(
            checked = false,
            onCheckedChange = {}
        )

        // ON 상태
        CommonToggle(
            checked = true,
            onCheckedChange = {}
        )
    }
}

