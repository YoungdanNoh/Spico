package com.a401.spicoandroid.common.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 그림자 색상 (#222222, 10% opacity)
val ShadowColor = Color(0x1A222222)

// DropShadow 1: Y = 1dp, Blur = 4dp
fun Modifier.dropShadow1(
    yOffset: Dp = 1.dp,
    blurRadius: Dp = 4.dp,
    color: Color = ShadowColor
): Modifier = this.then(
    Modifier.drawBehind {
        drawRect(
            color = color,
            topLeft = Offset(0f, yOffset.toPx()),
            size = Size(size.width, blurRadius.toPx())
        )
    }
)
