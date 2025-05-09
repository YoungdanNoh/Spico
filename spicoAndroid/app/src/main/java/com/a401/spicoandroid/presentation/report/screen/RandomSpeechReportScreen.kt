package com.a401.spicoandroid.presentation.report.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.a401.spicoandroid.common.ui.component.CommonTopBar

@Composable
fun RandomSpeechReportScreen(randomSpeechId: Int) {
    Scaffold(
        topBar = {
            CommonTopBar(centerText = "리포트")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("RandomSpeechReportScreen 스켈레톤")
        }
    }
}