package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.CommonTopBar

@Composable
fun RandomSpeechProjectListSelectScreen() {
    Scaffold(
        topBar = {
            CommonTopBar(centerText = "프로젝트 선택")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "RandomSpeechProjectSelectScreen")
        }
    }
}
