package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*

sealed class FeedbackType {
    data class FinalModeQnA(val text: String) : FeedbackType()
    data class CoachingMode(val message: String) : FeedbackType()
    data class Replay(val message: String) : FeedbackType()
}

@Composable
fun CommonFeedback(
    type: FeedbackType,
    modifier: Modifier = Modifier
) {
    when (type) {

        is FeedbackType.FinalModeQnA -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BackgroundPrimary)
                    .border(1.dp, LineTertiary, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }

        is FeedbackType.CoachingMode -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(240.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.character_coaching),
                    contentDescription = "코칭 캐릭터",
                    modifier = Modifier
                        .width(217.dp)
                        .height(208.dp)
                        .align(Alignment.TopCenter)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .widthIn(max = 400.dp)
                        .height(100.dp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                        .border(1.dp, LineTertiary, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        is FeedbackType.Replay -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.character_video),
                    contentDescription = "영상 다시보기 캐릭터",
                    modifier = Modifier
                        .width(85.dp)
                        .height(85.dp)
                        .padding(end = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                        .border(1.dp, LineTertiary, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = type.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewFinalModeQnA() {
    CommonFeedback(FeedbackType.FinalModeQnA("Q. 자기소개를 부탁드려요."))
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewCoachingMode() {
    CommonFeedback(FeedbackType.CoachingMode("말을 너무 빨리 하고 있어요."))
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewReplayFeedback() {
    CommonFeedback(FeedbackType.Replay("첫 부분에서 너무 빨랐어요.\n다시 확인해보세요."))
}
