package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.BrokenWhite
import com.a401.spicoandroid.common.ui.theme.TextSecondary
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.theme.White
import com.a401.spicoandroid.common.utils.formatDateWithDay
import com.a401.spicoandroid.presentation.randomspeech.dummy.DummyRandomSpeechList
import com.a401.spicoandroid.presentation.randomspeech.util.getTopicIconRes
import com.a401.spicoandroid.presentation.randomspeech.model.RandomSpeech
import androidx.compose.foundation.layout.Box

@Composable
fun RandomSpeechProjectListScreen(
    onProjectClick: (randomSpeechId: Int) -> Unit = {},
    onStartClick: () -> Unit = {}
) {
    val projectList = DummyRandomSpeechList // API로 대체 예정
//    val projectList = emptyList<RandomSpeech>()

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

        if (projectList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.character_home_1),
                        contentDescription = "랜덤스피치 없음 이미지",
                        modifier = Modifier.size(140.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "프로젝트가 없어요.\n랜덤스피치를 시작해보세요!",
                        style = Typography.titleLarge.copy(lineHeight = 28.sp),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(projectList) { item ->
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
}

