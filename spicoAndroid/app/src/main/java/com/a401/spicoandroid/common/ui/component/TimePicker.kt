package com.a401.spicoandroid.common.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePicker(
    minute: Int,
    second: Int,
    onTimeSelected: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    validate: (Int, Int) -> String?,
    guideText: String,
    timePickerDialog: @Composable (
        show: Boolean,
        onDismiss: () -> Unit,
        onTimeSelected: (Int, Int, Int) -> Unit
    ) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    var inputMinute by remember { mutableIntStateOf(minute) }
    var inputSecond by remember { mutableIntStateOf(second) }

    val errorMessage = validate(inputMinute, inputSecond)
    val isActive = inputMinute != 0 || inputSecond != 0

    fun validateAndSet(m: Int?, s: Int?) {
        val mm = m?.coerceIn(0, 59) ?: inputMinute
        val ss = s?.coerceIn(0, 59) ?: inputSecond
        inputMinute = mm
        inputSecond = ss
        onTimeSelected(0, mm, ss)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Placeholder, RoundedCornerShape(8.dp))
            .clickable { showPicker = true }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TimeUnitDisplay(inputMinute, isActive)
                    Text(" : ", style = Typography.titleLarge)
                    TimeUnitDisplay(inputSecond, isActive)
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_clock_text_tertiary),
                    contentDescription = "시계 아이콘",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showPicker = true }
                )
            }

            Text(
                text = errorMessage ?: guideText,
                style = Typography.bodySmall,
                color = if (errorMessage != null) Error else TextTertiary,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp, end = 4.dp)
            )
        }

        timePickerDialog(
            showPicker,
            { showPicker = false },
            { _, m, s ->
                validateAndSet(m, s)
                showPicker = false
            }
        )
    }
}

@Composable
fun TimeUnitDisplay(
    value: Int,
    isActive: Boolean
) {
    Text(
        text = value.toString().padStart(2, '0'),
        style = Typography.titleLarge,
        color = if (isActive) TextPrimary else TextTertiary,
        modifier = Modifier
            .width(48.dp)
            .height(48.dp),
        textAlign = TextAlign.Center
    )
}


@Composable
fun TimeUnitTextField(
    value: Int,
    onValueChange: (Int?) -> Unit,
    isError: Boolean,
    supportingText: String
) {
    val focusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf(value.toString().padStart(2, '0')) }

    LaunchedEffect(value) {
        if (text.toIntOrNull() != value) {
            text = value.toString().padStart(2, '0')
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            value = text,
            onValueChange = {
                if (it.length <= 2 && it.all { c -> c.isDigit() }) {
                    text = it
                    onValueChange(it.toIntOrNull())
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = Typography.titleLarge.copy(
                color = TextPrimary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) text = ""
                },
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    innerTextField()
                }
            }
        )
        Text(
            text = supportingText,
            style = Typography.bodySmall,
            color = if (isError) Error else TextTertiary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
