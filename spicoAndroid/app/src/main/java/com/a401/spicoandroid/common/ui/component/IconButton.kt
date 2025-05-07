package com.a401.spicoandroid.common.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    @DrawableRes iconResId: Int,
    contentDescription: String? = null,
    onClick: () -> Unit,
    size: Dp = 24.dp
) {
    Image(
        painter = painterResource(id = iconResId),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size)
            .semantics {
                contentDescription?.let { this.contentDescription = it }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentScale = ContentScale.Fit
    )
}
