package com.a401.spicoandroid.common.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonList(
    modifier: Modifier = Modifier,
    imagePainter: Painter? = null,
    title: String,
    description: String? = null,
    rightIcon: Painter? = null,
    titleStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null
) {
    val finalTitleStyle = titleStyle ?: Typography.displaySmall
    val finalDescriptionStyle = descriptionStyle ?: Typography.labelSmall

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(White)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .drawBehind {
                drawRect(
                    color = Color(0x1A222222),
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, 4f)
                )
            }
            .padding(16.dp),
        color = White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imagePainter != null) {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(46.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = finalTitleStyle,
                    color = TextPrimary
                )
                if (!description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = finalDescriptionStyle,
                        color = TextTertiary
                    )
                }
            }

            if (rightIcon != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = rightIcon,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview_All() {
    val leftImage = painterResource(id = R.drawable.img_art)         // 더미 이미지 리소스
    val rightArrow = painterResource(id = R.drawable.ic_arrow_right_black)

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. 기본 스타일 + 전체 요소 사용
        CommonList(
            imagePainter = leftImage,
            title = "관통 프로젝트",
            description = "2025.04.25. 금요일",
            rightIcon = rightArrow
        )

        // 2. 설명 생략 (한 줄 제목만)
        CommonList(
            imagePainter = leftImage,
            title = "기성세대와 MZ세대 갈등",
            rightIcon = rightArrow
        )

        // 3. 아이콘과 이미지 모두 없음 (텍스트만)
        CommonList(
            title = "코칭 모드 3회차",
            description = "자율 프로젝트"
        )

        // 4. 커스텀 스타일 적용
        CommonList(
            title = "전체적으로 좋아요! 잘하고 있어요!! 이대로 쭉 가봅시다\n할만하죠?? 저도 그렇게 생각합니다.",
            titleStyle = Typography.bodySmall
        )
    }
}
