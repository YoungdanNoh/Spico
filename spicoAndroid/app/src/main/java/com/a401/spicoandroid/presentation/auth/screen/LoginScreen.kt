package com.a401.spicoandroid.presentation.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.a401.spicoandroid.R
import com.a401.spicoandroid.presentation.auth.component.*
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
        listOf("코칭모드" to com.a401.spicoandroid.common.ui.theme.Hover, "로 실시간\n" to com.a401.spicoandroid.common.ui.theme.TextPrimary, "AI발표 분석을 받을 수 있어요!" to com.a401.spicoandroid.common.ui.theme.TextPrimary),
        listOf("파이널 모드" to com.a401.spicoandroid.common.ui.theme.Hover, "로 실전처럼\n" to com.a401.spicoandroid.common.ui.theme.TextPrimary, "발표하고 " to com.a401.spicoandroid.common.ui.theme.TextPrimary, "Q&A" to com.a401.spicoandroid.common.ui.theme.Hover, "까지" to com.a401.spicoandroid.common.ui.theme.TextPrimary),
        listOf("성량, 발음, 속도 등 한눈에\n" to com.a401.spicoandroid.common.ui.theme.TextPrimary, "확인하는 발표 " to com.a401.spicoandroid.common.ui.theme.TextPrimary, "리포트" to com.a401.spicoandroid.common.ui.theme.Hover),
        listOf("여러 주제를 접할 수 있는\n" to com.a401.spicoandroid.common.ui.theme.TextPrimary, "랜덤스피치" to com.a401.spicoandroid.common.ui.theme.Hover),
        listOf("Spico" to com.a401.spicoandroid.common.ui.theme.Hover, "와 함께\n" to com.a401.spicoandroid.common.ui.theme.TextPrimary, "완벽한 발표를 준비해봐요!" to com.a401.spicoandroid.common.ui.theme.TextPrimary)
    )

    val pagerState = rememberPagerState(initialPage = 0) { imageList.size }
    val scope = rememberCoroutineScope()

    // 자동 슬라이딩
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
                detectTapGestures {
                    scope.launch {
                        val next = (pagerState.currentPage + 1) % imageList.size
                        pagerState.scrollToPage(next)
                    }
                }
            }
    ) {
        // logo + img + text
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            LogoWithPagerSection(pagerState, imageList)
            LoginDescriptionText(pagerState.currentPage, textList)
        }

        // indicator+button
        LoginBottomSection(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentPage = pagerState.currentPage,
            totalCount = imageList.size,
            onSelect = { scope.launch { pagerState.scrollToPage(it) } },
            onKakaoLoginClick = onKakaoLoginClick
        )
    }
}
