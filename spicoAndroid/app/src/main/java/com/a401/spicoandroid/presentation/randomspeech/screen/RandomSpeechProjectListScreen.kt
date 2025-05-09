package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.BrokenWhite
import com.a401.spicoandroid.common.ui.theme.White

@Composable
fun RandomSpeechProjectListScreen(
    onProjectClick: (randomSpeechId: Int) -> Unit = {},
    onStartClick: () -> Unit = {}
) {
    // ❗ 실제 API 연결 전 더미 데이터
    val projectList = listOf(
        RandomSpeech(id = 1, topic = "economy", title = "경제 랜덤 스피치", dateTime = "2025.05.11 11:00"),
        RandomSpeech(id = 2, topic = "art", title = "예술 랜덤 스피치", dateTime = "2025.05.12 11:00"),
        RandomSpeech(id = 3, topic = "economy", title = "경제 랜덤 스피치 2", dateTime = "2025.05.13 11:00")
    )

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 목록",
                rightContent = {
                    CommonButton(
                        text = "시작",
                        size = ButtonSize.XS,
                        backgroundColor = Action,
                        borderColor = Action,
                        textColor = White,
                        onClick = onStartClick,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            )
        },
        containerColor = BrokenWhite
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            items(projectList) { item ->
                CommonList(
                    imagePainter = painterResource(id = getTopicIconRes(item.topic)),
                    title = item.title,
                    description = item.dateTime,
                    onClick = { onProjectClick(item.id) }
                )
            }
        }
    }
}

data class RandomSpeech(
    val id: Int,
    val topic: String,
    val title: String,
    val dateTime: String
)

// 토픽별 아이콘 매핑 함수
fun getTopicIconRes(topic: String): Int {
    return when (topic.lowercase()) {
        "politics" -> R.drawable.img_politics
        "economy" -> R.drawable.img_economy
        "it" -> R.drawable.img_it
        "sports" -> R.drawable.img_sports
        "nature" -> R.drawable.img_nature
        "culture" -> R.drawable.img_culture
        "society" -> R.drawable.img_society
        "science" -> R.drawable.img_science
        "art" -> R.drawable.img_art
        "health" -> R.drawable.img_health
        "history" -> R.drawable.img_history
        "environment" -> R.drawable.img_environment
        else -> R.drawable.img_list_practice // 없는 경우 기본 아이콘
    }
}

