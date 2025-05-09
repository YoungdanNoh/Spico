package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import com.a401.spicoandroid.common.ui.component.CommonTopBar

@Composable
fun RandomSpeechTopicSelectScreen() {
    Scaffold(
        topBar = {
            CommonTopBar(centerText = "주제 선택")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "RandomSpeechTopicSelectScreen")
        }
    }
}