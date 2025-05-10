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
fun RandomSpeechScreen(
    onFinish: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CommonTopBar(centerText = "랜덤스피치")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("RandomSpeechScreen 스켈레톤")
                Spacer(modifier = Modifier.height(16.dp))
                CommonButton(text = "종료", onClick = onFinish)
            }
        }
    }
}