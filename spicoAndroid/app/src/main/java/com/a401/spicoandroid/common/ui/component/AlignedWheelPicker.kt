package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.TextTertiary
import com.a401.spicoandroid.common.ui.theme.Typography

@Composable
fun CustomWheelPicker(
    values: List<Int>,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    valueColorOverride: ((Int) -> Color)? = null,
    enabledCheck: ((Int) -> Boolean)? = null
) {
    val ITEM_HEIGHT = 40.dp
    val density = LocalDensity.current
    val offsetPx = with(density) { ITEM_HEIGHT.toPx() }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = values.indexOf(selectedValue).coerceAtLeast(0)
    )

    LaunchedEffect(listState.isScrollInProgress.not()) {
        val offsetIndex = listState.firstVisibleItemIndex
        val offsetY = listState.firstVisibleItemScrollOffset
        val centerIndex = if (offsetY > offsetPx / 2) offsetIndex + 1 else offsetIndex
        listState.animateScrollToItem(centerIndex)
        values.getOrNull(centerIndex)?.let {
            if (enabledCheck?.invoke(it) != false) {
                onValueChange(it)
            }
        }
    }

    Box(modifier = Modifier.height(150.dp).width(64.dp)) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            itemsIndexed(values) { index, value ->
                val isSelected = value == selectedValue
                val textColor = valueColorOverride?.invoke(value)
                    ?: if (isSelected) TextPrimary else TextTertiary

                Text(
                    text = value.toString().padStart(2, '0'),
                    style = if (isSelected) Typography.displayMedium else Typography.titleLarge,
                    color = textColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = if (isSelected) 12.dp else 6.dp),
                    textAlign = TextAlign.Center
                )

                if (index == values.lastIndex) {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun AlignedColon() {
    Box(
        modifier = Modifier
            .width(20.dp)
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = ":",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(y = (-5).dp)
        )
    }
}
