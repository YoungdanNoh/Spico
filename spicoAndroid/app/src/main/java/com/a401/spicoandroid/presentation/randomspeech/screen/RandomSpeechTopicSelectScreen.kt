package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.randomspeech.component.TopicItem
import com.a401.spicoandroid.common.ui.component.BackIconButton

@Composable
fun RandomSpeechTopicSelectScreen(
    navController: NavController
) {
    val topics = listOf(
        "politics", "economy", "it", "sports", "nature", "culture",
        "society", "science", "art", "health", "history", "environment"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 20.dp)
    ) {
        // 상단바
        CommonTopBar(
            centerText = "주제 선택",
            leftContent = { BackIconButton(navController) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 안내 텍스트
        Text(
            text = "어떤 주제로 말해볼까요?",
            style = Typography.displayMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 주제 버튼 그리드 (바로 이동)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(topics) { topic ->
                TopicItem(
                    topic = topic,
                    isSelected = false, // 선택 상태 의미 없음
                    onClick = {
                        navController.navigate("random_speech_setting/$topic")
                    }
                )
            }
        }
    }
}