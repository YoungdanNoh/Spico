package com.a401.spicoandroid.presentation.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FeedbackCard(
    imageResId: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow1()
            .height(210.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = White,
                        shape = CircleShape
                    )
                    .border(5.dp, Disabled, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = title,
                    modifier = Modifier.size(32.dp) // 이미지 안쪽 크기
                )
            }

            // 제목
            Text(
                text = title,
                style = Typography.displayMedium.copy(color = Action),
                modifier = Modifier.padding(12.dp)
            )

            // 설명 텍스트 (말풍선 느낌)
            Box(
                modifier = Modifier
                    .background(
                        color = Action,
                        shape = RoundedCornerShape(30)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = description,
                    style = Typography.bodyLarge.copy(color = White),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun FeedbackCardPreview() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){

    FeedbackCard(
        imageResId = com.a401.spicoandroid.R.drawable.img_feedback_volume,
        title = "성량",
        description = "전반적으로 괜찮았지만,\n좀 더 일정하게 유지해보세요"
    )
    }
}
