package com.a401.spicoandroid.common.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 그림자 색상 (#222222, 10% opacity)
val ShadowColor = Color(0x26222222)

// DropShadow 1: Y = 1dp, Blur = 4dp
fun Modifier.dropShadow1(
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 8.dp,
    color: Color = ShadowColor
): Modifier = this.shadow(
    elevation = elevation,
    shape = RoundedCornerShape(cornerRadius),
    ambientColor = color,
    spotColor = color
)
