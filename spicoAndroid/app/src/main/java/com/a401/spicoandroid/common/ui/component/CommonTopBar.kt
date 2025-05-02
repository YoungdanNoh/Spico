package com.a401.spicoandroid.common.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.presentation.navigation.LocalNavController

@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    text: String,
    showBackButton: Boolean = true,
    actionImageId: Int? = null,
    onActionClick: (() -> Unit)? = null
) {
    val navController = LocalNavController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            if (showBackButton) {
                ImageButton(
                    modifier,
                    onClick = { navController.popBackStack() },
                    R.drawable.arrow_left
                )
            }
            Text(text = text)
            onActionClick?.takeIf { actionImageId != null }?.let {
                ImageButton(
                    modifier,
                    onClick = it,
                    imageId = actionImageId!!
                )
            }
        }
    }
}

@Composable
fun ImageButton(modifier: Modifier, onClick: () -> Unit, @DrawableRes imageId: Int) {
    Image(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick() }
            ),
        painter = painterResource(id = imageId),
        contentDescription = "image button",
    )
}

@Preview(showBackground = true)
@Composable
fun CommonTopBarPreview() {
    val fakeNavController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides fakeNavController) {
        Column() {
            CommonTopBar(
                text = "프리뷰",
                showBackButton = true,
                onActionClick = {},
                actionImageId = R.drawable.ic_launcher_foreground
            )
            CommonTopBar(
                text = "프리뷰",
                onActionClick = {},
                actionImageId = R.drawable.ic_launcher_foreground
            )
            CommonTopBar(
                text = "프리뷰",
                showBackButton = true
            )
            CommonTopBar(
                text = "프리뷰"
            )
        }
    }
}