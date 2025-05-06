package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.TextTertiary
import com.a401.spicoandroid.common.ui.theme.Typography

@Composable
fun CustomOverlayTimePicker(
    initialHour: Int,
    initialMinute: Int,
    initialSecond: Int,
    onDismiss: () -> Unit,
    onTimeSelected: (Int, Int, Int) -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }
    var selectedSecond by remember { mutableIntStateOf(initialSecond) }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x29222222))
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
                        AlignedWheelPicker(
                            values = (0..23).toList(),
                            selectedValue = selectedHour,
                            onValueChange = { selectedHour = it }
                        )
                        AlignedColon()
                        AlignedWheelPicker(
                            values = (0..59).toList(),
                            selectedValue = selectedMinute,
                            onValueChange = { selectedMinute = it }
                        )
                        AlignedColon()
                        AlignedWheelPicker(
                            values = (0..59).toList(),
                            selectedValue = selectedSecond,
                            onValueChange = { selectedSecond = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //TODO: 컴포넌트 버튼으로 바꾸기
                    Text(
                        text = "확인",
                        modifier = Modifier
                            .clickable {
                                onTimeSelected(selectedHour, selectedMinute, selectedSecond)
                                onDismiss()
                            }
                            .padding(vertical = 8.dp, horizontal = 24.dp),
                        style = Typography.titleLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun AlignedColon() {
    Box(
        modifier = Modifier
            .width(32.dp)
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = ":",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AlignedWheelPicker(
    values: List<Int>,
    selectedValue: Int,
    onValueChange: (Int) -> Unit
) {
    val ITEM_HEIGHT = 40.dp
    val density = LocalDensity.current
    val offsetPx = with(density) { ITEM_HEIGHT.toPx() }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = values.indexOf(selectedValue).coerceAtLeast(0)
    )

    LaunchedEffect(listState.isScrollInProgress.not()) {
        val centerIndex = listState.firstVisibleItemIndex +
                if (listState.firstVisibleItemScrollOffset > offsetPx / 2) 1 else 0
        listState.animateScrollToItem(centerIndex)
        values.getOrNull(centerIndex)?.let { onValueChange(it) }
    }

    Box(modifier = Modifier.height(150.dp).width(64.dp)) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            itemsIndexed(values) { _, value ->
                val isSelected = value == selectedValue

                Text(
                    text = value.toString().padStart(2, '0'),
                    style = if (isSelected) Typography.displayMedium else Typography.titleLarge,
                    color = if (isSelected) TextPrimary else TextTertiary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = if (isSelected) 12.dp else 6.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
