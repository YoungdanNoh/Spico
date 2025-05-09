package com.a401.spicoandroid.presentation.randomspeech.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.LocalNavController

@Composable
fun RandomSpeechLandingScreen(
    navController: NavController = LocalNavController.current,
    onStartClick: () -> Unit = { /* TODO: Navigate to topic select */ },
    onProjectClick: () -> Unit = { /* TODO: Navigate to project select */ }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrokenWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 바
        CommonTopBar(
            centerText = "랜덤스피치",
            rightContent = {
                CommonButton(
                    text = "시작",
                    size = ButtonSize.XS,
                    backgroundColor = Action,
                    borderColor = Action,
                    textColor = White,
                    onClick = onStartClick
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 중앙 이미지
        Image(
            painter = painterResource(id = R.drawable.img_random_main),
            contentDescription = "랜덤스피치 아이콘 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp)

        )

        Spacer(modifier = Modifier.weight(1f))

        // 하단 버튼
        CommonButton(
            text = "프로젝트 목록",
            size = ButtonSize.LG,
            backgroundColor = White,
            borderColor = Action,
            textColor = Action,
            onClick = onProjectClick,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RandomSpeechLandingScreenPreview() {
    SpeakoAndroidTheme {
        RandomSpeechLandingScreen(
            navController = rememberNavController(),
            onStartClick = { println("시작 클릭됨") },
            onProjectClick = { println("프로젝트 목록 클릭됨") }
        )
    }
}