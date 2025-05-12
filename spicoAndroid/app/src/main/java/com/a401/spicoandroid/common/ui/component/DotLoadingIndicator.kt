package com.a401.spicoandroid.common.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.Disabled
import kotlinx.coroutines.delay

@Composable
fun DotLoadingIndicator(
    dotCount: Int = 5,
    dotSize: Dp = 8.dp,
    dotSpacing: Dp = 6.dp,
    activeDotScale: Float = 1.5f,
    animationDuration: Int = 300,
    animationDelay: Long = 100L
) {
    val dotScales = remember { List(dotCount) { Animatable(1f) } }
    var currentActiveIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            for (i in dotScales.indices) {
                currentActiveIndex = i
                dotScales[i].animateTo(
                    targetValue = activeDotScale,
                    animationSpec = tween(durationMillis = animationDuration)
                )
                dotScales[i].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = animationDuration)
                )
                delay(animationDelay)
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(dotSpacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentSize()
    ) {
        dotScales.forEachIndexed { index, scale ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value
                    )
                    .clip(CircleShape)
                    .background(
                        if (index == currentActiveIndex) Action else Disabled
                    )
            )
        }
    }
}

