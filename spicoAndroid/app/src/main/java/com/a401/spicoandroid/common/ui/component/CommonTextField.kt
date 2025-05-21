package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    height: Dp = 56.dp,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .height(height),
            textStyle = Typography.titleLarge.copy(color = TextPrimary),
            placeholder = {
                Text(
                    text = placeholder,
                    style = Typography.titleLarge.copy(color = TextTertiary)
                )
            },
            shape = RoundedCornerShape(8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTextFieldPreview() {
    CommonTextField(
        value = "",
        onValueChange = {},
        placeholder = "텍스트를 입력해주세요",
        height = 56.dp
    )
}
