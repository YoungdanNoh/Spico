package com.a401.spicoandroid.presentation.randomspeech.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.formatDateTimeWithDay
import com.a401.spicoandroid.presentation.navigation.LocalNavController
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.randomspeech.component.RandomReportCard
import com.a401.spicoandroid.presentation.randomspeech.component.RandomReportDeleteAlert
import com.a401.spicoandroid.presentation.randomspeech.component.RandomReportDeleteBottomSheet
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechListViewModel

@Composable
fun RandomSpeechListScreen(
    viewModel: RandomSpeechListViewModel,
    onProjectClick: (randomSpeechId: Int) -> Unit = {},
    onStartClick: () -> Unit = {}
) {
    val navController = LocalNavController.current

    val speechList = viewModel.speechList
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    var selectedId by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchList()
    }

    LaunchedEffect(viewModel.deleteSuccess) {
        if (viewModel.deleteSuccess) {
            Toast.makeText(context, "리포트가 삭제되었습니다", Toast.LENGTH_SHORT).show()
            viewModel.resetDeleteSuccess()
        }
    }

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

    // 삭제 알림창
    if (showAlert) {
        RandomReportDeleteAlert(
            onConfirm = {
                selectedId?.let { viewModel.deleteItem(it) }
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
                leftContent = {
                    BackIconButton {
                        navController.navigate(NavRoutes.RandomSpeechLanding.route) {
                            popUpTo(NavRoutes.RandomSpeechLanding.route) { inclusive = false }
                        }
                    }
                },
                rightContent = {
                    CommonButton(
                        text = "시작",
                        size = ButtonSize.XS,
                        backgroundColor = Action,
                        borderColor = Action,
                        textColor = White,
                        onClick = {
                            navController.navigate(NavRoutes.RandomSpeechTopicSelect.route)
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            )
        },
        containerColor = BrokenWhite
    ) { innerPadding ->

        when {
            isLoading -> {
                LoadingInProgressView(
                    imageRes = com.a401.spicoandroid.R.drawable.character_home_5,
                    message = "로딩 중입니다...",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            speechList.isEmpty() -> {
                EmptyStateView(
                    imageRes = com.a401.spicoandroid.R.drawable.character_home_1,
                    message = "리포트가 없어요.\n랜덤스피치를 시작해보세요!",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(speechList) { item ->
                        RandomReportCard(
                            id = item.id,
                            topic = item.topic,
                            title = item.title,
                            dateTime = formatDateTimeWithDay(item.dateTime),
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
}
