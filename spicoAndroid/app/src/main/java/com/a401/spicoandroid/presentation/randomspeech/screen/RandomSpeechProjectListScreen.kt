package com.a401.spicoandroid.presentation.randomspeech.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.BrokenWhite
import com.a401.spicoandroid.common.ui.theme.White
import com.a401.spicoandroid.common.utils.formatDateWithDay
import com.a401.spicoandroid.presentation.randomspeech.dummy.DummyRandomSpeechList
import com.a401.spicoandroid.presentation.randomspeech.util.getTopicIconRes
import com.a401.spicoandroid.presentation.randomspeech.model.RandomSpeech

@Composable
fun RandomSpeechProjectListScreen(
    onProjectClick: (randomSpeechId: Int) -> Unit = {},
    onStartClick: () -> Unit = {}
) {
    val projectList = DummyRandomSpeechList // API로 대체 예정

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
            items(items = projectList) { item ->
                CommonList(
                    imagePainter = painterResource(id = getTopicIconRes(item.topic)),
                    title = item.title,
                    description = formatDateWithDay(item.dateTime),
                    onClick = { onProjectClick(item.id) }
                )
            }

        }
    }
}

