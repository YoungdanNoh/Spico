package com.a401.spicoandroid.presentation.report.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun AudioPlayerCard(
    title: String,
    currentPositionText: String,
    durationText: String,
    isPlaying: Boolean,
    progress: Float,
    onPlayPauseClick: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow1()
            .height(108.dp),
        color = White,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Action)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Action, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.ic_pause_white else R.drawable.ic_play_white
                    ),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = White
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = Typography.titleLarge.copy(color = TextPrimary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Slider(
                    value = progress.coerceIn(0f, 1f),
                    onValueChange = { onSeek(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Hover,
                        activeTrackColor = Action,
                        inactiveTrackColor = Disabled
                    ),
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = currentPositionText,
                        style = Typography.bodySmall.copy(color = TextTertiary)
                    )
                    Text(
                        text = durationText,
                        style = Typography.bodySmall.copy(color = TextTertiary)
                    )
                }
            }
        }
    }
}
