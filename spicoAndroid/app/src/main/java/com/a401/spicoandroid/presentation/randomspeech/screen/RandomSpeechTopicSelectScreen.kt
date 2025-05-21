package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.common.ui.component.BackIconButton
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.domain.randomspeech.model.RandomSpeechTopic
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.randomspeech.component.TopicItem
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechSharedViewModel

@Composable
fun RandomSpeechTopicSelectScreen(
    navController: NavController,
    viewModel: RandomSpeechSharedViewModel
) {
    val topics = RandomSpeechTopic.entries.toList()

    // 진입 시 랜덤 스피치 상태 초기화
    LaunchedEffect(Unit) {
        viewModel.reset()
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "주제 선택",
                leftContent = { BackIconButton(navController) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BrokenWhite)
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "어떤 주제로 \n연습하고 싶은가요?",
                style = Typography.displayMedium.copy(letterSpacing = 1.sp),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "원하는 주제를 선택하세요.",
                style = Typography.bodyLarge,
                color = TextTertiary
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(BrokenWhite)
            ) {
                items(topics) { topic ->
                    TopicItem(
                        topic = topic,
                        isSelected = false,
                        onClick = {
                            viewModel.setTopic(topic)
                            navController.navigate(NavRoutes.RandomSpeechSetting.route)
                        }
                    )
                }
            }
        }
    }
}
