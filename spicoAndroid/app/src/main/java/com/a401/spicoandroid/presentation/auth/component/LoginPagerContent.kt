package com.a401.spicoandroid.presentation.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.theme.BrokenWhite
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.R

@Composable
fun LoginPagerContent(
    pagerState: PagerState,
    imageList: List<Int>,
    textList: List<List<Pair<String, Color>>>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 고정 로고
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrokenWhite)
                .padding(top = 64.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_medium),
                contentDescription = "Spico Logo"
            )
        }

        // 슬라이딩 이미지 + 텍스트
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // 나머지 영역 차지
        ) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                // 이미지 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrokenWhite)
                        .padding(top = 32.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageList[page]),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(356.dp)
                    )
                }

                // 텍스트 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                ) {
                    val richText = buildAnnotatedString {
                        textList[page].forEach { (text, color) ->
                            withStyle(Typography.displayMedium.copy(color = color).toSpanStyle()) {
                                append(text)
                            }
                        }
                    }

                    Text(
                        text = richText,
                        style = Typography.displayMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

