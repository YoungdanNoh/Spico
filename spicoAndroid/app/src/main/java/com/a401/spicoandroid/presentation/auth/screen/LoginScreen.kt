package com.a401.spicoandroid.presentation.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.CommonCircularProgressBar
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.auth.component.KakaoLoginButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onKakaoLoginClick: () -> Unit
) {
    val imageList = listOf(
        R.drawable.img_login1,
        R.drawable.img_login2,
        R.drawable.img_login3,
        R.drawable.img_login4,
        R.drawable.character_login
    )

    val textList = listOf(
        listOf("코칭모드" to Hover, "로 실시간\n" to TextPrimary, "AI발표 분석을 받을 수 있어요!" to TextPrimary),
        listOf("파이널 모드" to Hover, "로 실전처럼\n" to TextPrimary, "발표하고 " to TextPrimary, "Q&A" to Hover, "까지" to TextPrimary),
        listOf("성량, 발음, 속도 등 한눈에\n" to TextPrimary, "확인하는 발표 " to TextPrimary, "리포트" to Hover),
        listOf("여러 주제를 접할 수 있는\n" to TextPrimary, "랜덤스피치" to Hover),
        listOf("Spico" to Hover, "와 함께\n" to TextPrimary, "완벽한 발표를 준비해봐요!" to TextPrimary)
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageList.size }
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000L)
            scope.launch {
                val next = (pagerState.currentPage + 1) % imageList.size
                pagerState.scrollToPage(next)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        scope.launch {
                            val next = (pagerState.currentPage + 1) % imageList.size
                            pagerState.scrollToPage(next)
                        }
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color.White)
        ) {
            // 상단: 로고 + 이미지 (BrokenWhite)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrokenWhite)
                    .padding(top = 64.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_medium),
                        contentDescription = "Spico Logo"
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                    ) { page ->
                        Image(
                            painter = painterResource(id = imageList[page]),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(360.dp)
                        )
                    }
                }
            }

            // 하단 텍스트 (White)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 32.dp)
            ) {
                val richText = buildAnnotatedString {
                    textList[pagerState.currentPage].forEach { (text, color) ->
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

        // ✅ 하단 고정 영역은 클릭 영향 안 받도록 별도 Column
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonCircularProgressBar(
                selectedIndex = pagerState.currentPage,
                onSelect = { index ->
                    scope.launch { pagerState.scrollToPage(index) }
                },
                totalCount = imageList.size
            )

            Spacer(modifier = Modifier.height(24.dp))

            KakaoLoginButton(onClick = onKakaoLoginClick)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SpeakoAndroidTheme {
        LoginScreen(onKakaoLoginClick = {})
    }
}
