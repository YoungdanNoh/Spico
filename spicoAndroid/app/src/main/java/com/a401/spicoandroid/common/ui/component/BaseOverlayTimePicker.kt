package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.a401.spicoandroid.common.ui.theme.Error
import com.a401.spicoandroid.common.ui.theme.OverlayDark20
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.TextTertiary
import com.a401.spicoandroid.common.ui.theme.Typography

@Composable
fun BaseOverlayTimePicker(
    minuteRange: List<Int>,
    secondRange: List<Int>,
    initialMinute: Int,
    initialSecond: Int,
    validate: (minute: Int, second: Int) -> String?,
    guideText: String,
    onDismiss: () -> Unit,
    onTimeSelected: (Int, Int, Int) -> Unit,
    secondColor: (selected: Int, current: Int) -> Color = { selected, current ->
        if (current == selected) TextPrimary else TextTertiary
    }
) {
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }
    var selectedSecond by remember { mutableIntStateOf(initialSecond) }

    val errorMessage = validate(selectedMinute, selectedSecond)
    val isSecondFixed = secondRange.size == 1 && secondRange.first() == 0

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OverlayDark20)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.9f)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(vertical = 24.dp)
                    .clickable(enabled = false) {}
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomWheelPicker(
                            values = minuteRange,
                            selectedValue = selectedMinute,
                            onValueChange = { selectedMinute = it }
                        )
                        AlignedColon()
                        CustomWheelPicker(
                            values = secondRange,
                            selectedValue = selectedSecond,
                            onValueChange = { selectedSecond = it },
                            valueColorOverride = { secondColor(selectedSecond, it) },
                            enabledCheck = { _ -> !isSecondFixed }
                        )
                    }

                    Text(
                        text = errorMessage ?: guideText,
                        style = Typography.bodySmall,
                        color = if (errorMessage != null) Error else TextTertiary,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CommonButton(
                        text = "확인",
                        size = ButtonSize.MD,
                        onClick = {
                            if (errorMessage == null) {
                                onTimeSelected(0, selectedMinute, selectedSecond)
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}