package com.a401.spicoandroid.presentation.project.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectScriptViewModel

@Composable
fun ProjectScriptDetailScreen(
    navController: NavController,
    viewModel: ProjectScriptViewModel = hiltViewModel(),
    onEdit: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            CommonTopBar(
                centerText = "대본",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        onClick = { navController.popBackStack() }
                    )
                },
                rightContent = {
                    CommonButton(
                        text = "편집",
                        backgroundColor = White,
                        borderColor = Action,
                        textColor = Action,
                        size = ButtonSize.XS,
                        onClick = onEdit
                    )
                },
            )
        },
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = uiState.title,
                style = Typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.paragraphs.isEmpty()) {
                        item {
                            Text(
                                text = "등록된 대본이 없습니다.",
                                style = Typography.bodyLarge,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        items(uiState.paragraphs) { paragraph ->
                            Text(
                                text = paragraph.text,
                                style = Typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Justify
                            )
                        }
                    }
                }
            }
        }
    }
}

