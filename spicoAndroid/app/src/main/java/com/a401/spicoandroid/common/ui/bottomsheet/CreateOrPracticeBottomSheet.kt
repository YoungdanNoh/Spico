package com.a401.spicoandroid.common.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.*

@Composable
fun CreateOrPracticeBottomSheet(
    onCreateProjectClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BaseModalBottomSheet(onDismissRequest = onDismissRequest) {
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(156.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 프로젝트 생성 버튼
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BackgroundPrimary)
                    .clickable { onCreateProjectClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "프로젝트 생성",
                    style = Typography.displayMedium,
                    color = TextPrimary
                )
            }

            // 연습하기 버튼
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BackgroundPrimary)
                    .clickable { onPracticeClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "연습하기",
                    style = Typography.displayMedium,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
fun CreateOrPracticeBottomSheetPreview_DesignOnly() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = White,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            // 드래그 핸들
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(LineTertiary)
                )
            }

            // 버튼 2개
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(156.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "프로젝트 생성",
                        style = Typography.displayMedium,
                        color = TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "연습하기",
                        style = Typography.displayMedium,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
