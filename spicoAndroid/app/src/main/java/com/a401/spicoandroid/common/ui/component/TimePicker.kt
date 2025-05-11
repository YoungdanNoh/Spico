package com.a401.spicoandroid.common.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.a401.spicoandroid.common.ui.theme.Placeholder
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePicker(
    hour: Int,
    minute: Int,
    second: Int,
    onTimeSelected: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }

    var inputMinute by remember { mutableIntStateOf(minute) }
    var inputSecond by remember { mutableIntStateOf(second) }

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TimeUnitTextField(value = inputMinute, onValueChange = { validateAndSet(it, null) })
                Text(" : ", style = Typography.titleLarge)
                TimeUnitTextField(value = inputSecond, onValueChange = { validateAndSet(null, it) })
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

        if (showPicker) {
            CustomOverlayTimePicker(
                initialHour = 0,
                initialMinute = inputMinute,
                initialSecond = inputSecond,
                onDismiss = { showPicker = false },
                onTimeSelected = { _, m, s ->
                    validateAndSet(m, s)
                    showPicker = false
                }
            )
        }
    }
}

@Composable
fun TimeUnitTextField(
    value: Int,
    onValueChange: (Int?) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf(value.toString().padStart(2, '0')) }

    LaunchedEffect(value) {
        if (text.toIntOrNull() != value) {
            text = value.toString().padStart(2, '0')
        }
    }

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
                if (it.isFocused) {
                    text = ""
                }
            }
            .padding(0.dp),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                innerTextField()
            }
        }
    )
}
