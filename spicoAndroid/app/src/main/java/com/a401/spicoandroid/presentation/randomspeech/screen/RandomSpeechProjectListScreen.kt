package com.a401.spicoandroid.presentation.randomspeech.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.LocalNavController
import com.a401.spicoandroid.presentation.randomspeech.component.RandomReportCard
import com.a401.spicoandroid.presentation.randomspeech.component.RandomReportDeleteAlert
import com.a401.spicoandroid.presentation.randomspeech.component.RandomReportDeleteBottomSheet
import com.a401.spicoandroid.presentation.randomspeech.dummy.DummyRandomSpeechList
import com.a401.spicoandroid.presentation.randomspeech.model.RandomSpeech

@Composable
fun RandomSpeechProjectListScreen(
    onProjectClick: (randomSpeechId: Int) -> Unit = {},
    onStartClick: () -> Unit = {}
) {
    val projectList: List<RandomSpeech> = DummyRandomSpeechList
//    val projectList = emptyList<RandomSpeech>()
    val navController = LocalNavController.current

    var selectedId by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }

    // 바텀시트
    if (showBottomSheet) {
        RandomReportDeleteBottomSheet(
            onDeleteClick = {
                showBottomSheet = false
                showAlert = true
            },
            onDismissRequest = { showBottomSheet = false }
        )
    }

    // 알림창
    if (showAlert) {
        RandomReportDeleteAlert(
            onConfirm = {
                // TODO: 삭제 로직
                showAlert = false
            },
            onCancel = { showAlert = false },
            onDismiss = { showAlert = false }
        )
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "리포트 목록",
                leftContent = { BackIconButton(navController) },
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
            EmptyStateView(
                imageRes = com.a401.spicoandroid.R.drawable.character_home_1,
                message = "리포트가 없어요.\n랜덤스피치를 시작해보세요!",
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(projectList) { item ->
                    RandomReportCard(
                        id = item.id,
                        topic = item.topic,
                        title = item.title,
                        dateTime = item.dateTime,
                        onClick = onProjectClick,
                        onLongClick = {
                            selectedId = item.id
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
    }
}
