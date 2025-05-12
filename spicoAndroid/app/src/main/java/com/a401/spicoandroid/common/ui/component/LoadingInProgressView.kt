package com.a401.spicoandroid.common.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun LoadingInProgressView(
    @DrawableRes imageRes: Int,
    message: String,
    modifier: Modifier = Modifier,
    onHomeClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrokenWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "loading",
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        DotLoadingIndicator()

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = message,
            style = Typography.titleLarge.copy(lineHeight = 28.sp),
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        if (onHomeClick != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "홈으로 가기",
                style = Typography.titleMedium,
                color = TextTertiary,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onHomeClick()
                }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun LoadingInProgressViewPreview() {
    SpeakoAndroidTheme {
        LoadingInProgressView(
            imageRes = R.drawable.character_home_5,
            message = "리포트를 생성중이에요.\n잠시만 기다려주세요!",
            onHomeClick = { println("홈으로 이동!") }
        )
    }
}

