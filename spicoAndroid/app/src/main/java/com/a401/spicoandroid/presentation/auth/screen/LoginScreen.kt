package com.a401.spicoandroid.presentation.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.component.CommonCircularProgressBar
import com.a401.spicoandroid.presentation.auth.component.KakaoLoginButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    onKakaoLoginClick: () -> Unit
) {
    val imageList = listOf(
        R.drawable.img_login1,
        R.drawable.img_login2,
        R.drawable.img_login3,
        R.drawable.img_login4
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageList.size }
    )

    val scope = rememberCoroutineScope()

    // 자동 슬라이드 효과
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000L)
            val nextPage = (pagerState.currentPage + 1) % imageList.size
            scope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val screenHeight = maxHeight
        val upperHeight = screenHeight * 0.6f
        val lowerHeight = screenHeight * 0.4f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ✅ 상단 영역 (로고 + 캐러셀)
            Box(
                modifier = Modifier
                    .height(upperHeight)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logo_medium),
                        contentDescription = "Spico Logo"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) { page ->
                        Image(
                            painter = painterResource(id = imageList[page]),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // ✅ 하단 영역 (문구 + 인디케이터 + 버튼)
            Column(
                modifier = Modifier
                    .height(lowerHeight)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "여러 주제를 전환할 수 있는",
                    style = Typography.displayMedium,
                    color = TextPrimary
                )
                Text(
                    text = "랜덤스피치",
                    style = Typography.displayMedium,
                    color = Action
                )

                Spacer(modifier = Modifier.height(16.dp))

                CommonCircularProgressBar(
                    selectedIndex = pagerState.currentPage,
                    onSelect = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    totalCount = imageList.size
                )

                Spacer(modifier = Modifier.height(24.dp))

                KakaoLoginButton(onClick = onKakaoLoginClick)
            }
        }
    }
}
