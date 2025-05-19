package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.LocalNavController
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun RandomSpeechLandingScreen(
    navController: NavController = LocalNavController.current,
    onStartClick: () -> Unit = { navController.navigate(NavRoutes.RandomSpeechTopicSelect.route) },
    onProjectClick: () -> Unit = { navController.navigate(NavRoutes.RandomSpeechList.route) }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrokenWhite)
    ) {
        // 상단바 고정
        CommonTopBar(centerText = "랜덤스피치")

        // 이미지와 버튼만 스크롤 가능 영역
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.img_random_main),
                contentDescription = "랜덤스피치 아이콘 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(560.dp)
                    .padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CommonButton(
                    text = "리포트 목록",
                    size = ButtonSize.SM,
                    backgroundColor = White,
                    borderColor = Action,
                    textColor = Action,
                    onClick = onProjectClick,
                    modifier = Modifier.weight(1f)
                )

                CommonButton(
                    text = "시작하기",
                    size = ButtonSize.SM,
                    backgroundColor = Action,
                    borderColor = Action,
                    textColor = White,
                    onClick = onStartClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()))
        }
    }
}

