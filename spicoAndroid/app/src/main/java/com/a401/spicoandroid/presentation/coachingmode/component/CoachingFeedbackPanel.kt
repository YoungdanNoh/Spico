package com.a401.spicoandroid.presentation.coachingmode.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun CoachingFeedbackPanel(
    modifier: Modifier = Modifier,
    characterPainter: Painter,
    latestFeedback: String?
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        // 캐릭터 이미지
        Image(
            painter = characterPainter,
            contentDescription = "코칭 캐릭터",
            modifier = modifier.padding(bottom = 32.dp)
        )

        if (!latestFeedback.isNullOrBlank()) {
            Surface(
                color = White,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .dropShadow1()
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = latestFeedback,
                        style = Typography.titleLarge.copy(color = TextPrimary),
                        maxLines = 3,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CommonFeedbackPreview() {
    CoachingFeedbackPanel(
        characterPainter = painterResource(id = R.drawable.character_coaching),
        latestFeedback = "조금 더 천천히 말해볼까요?"
    )
}