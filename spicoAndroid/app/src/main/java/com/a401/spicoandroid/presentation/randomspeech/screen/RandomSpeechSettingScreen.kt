package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTopBar

@Composable
fun RandomSpeechSettingScreen(
    onNext: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CommonTopBar(centerText = "랜덤스피치 설정")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("RandomSpeechSettingScreen 스켈레톤")
                Spacer(modifier = Modifier.height(16.dp))
                CommonButton(text = "다음", onClick = onNext)
            }
        }
    }
}
