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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun EmptyStateView(
    @DrawableRes imageRes: Int,
    message: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BrokenWhite,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    subActionText: String? = null,
    onSubActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "empty",
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = message,
            style = Typography.titleLarge.copy(lineHeight = 28.sp),
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        if (!actionText.isNullOrBlank() && onActionClick != null) {
            Spacer(modifier = Modifier.height(32.dp))
            CommonButton(
                text = actionText,
                size = ButtonSize.LG,
                onClick = onActionClick
            )
        }

        if (!subActionText.isNullOrBlank() && onSubActionClick != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = subActionText,
                style = Typography.titleMedium,
                color = TextTertiary,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onSubActionClick()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStateView_NoAction_Preview() {
    SpeakoAndroidTheme {
        EmptyStateView(
            imageRes = R.drawable.character_home_1,
            message = "리포트가 없어요.\n랜덤스피치를 시작해보세요!"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStateView_WithAction_Preview() {
    SpeakoAndroidTheme {
        EmptyStateView(
            imageRes = R.drawable.character_home_1,
            message = "리포트가 없어요.\n랜덤스피치를 시작해보세요!",
            actionText = "랜덤스피치 시작",
            onActionClick = { println("시작 버튼 클릭됨") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStateView_WithActionAndSubAction_Preview() {
    SpeakoAndroidTheme {
        EmptyStateView(
            imageRes = R.drawable.character_home_2,
            message = "요청하신 페이지를 찾을 수 없어요.",
            actionText = "다시 시도하기",
            subActionText = "홈으로 가기",
            onSubActionClick = { println("홈으로 이동") }
        )
    }
}
