package com.a401.spicoandroid.common.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.R

@Composable
fun EmptyStateView(
    @DrawableRes imageRes: Int,
    message: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BrokenWhite,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
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

        // 버튼이 필요한 경우에만 표시
        if (!actionText.isNullOrBlank() && onActionClick != null) {
            Spacer(modifier = Modifier.height(32.dp))
            CommonButton(
                text = actionText,
                size = ButtonSize.LG,
                onClick = onActionClick
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


