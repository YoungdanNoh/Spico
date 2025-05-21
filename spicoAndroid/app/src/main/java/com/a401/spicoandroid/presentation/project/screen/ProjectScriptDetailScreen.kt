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
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectDetailViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectScriptViewModel

@Composable
fun ProjectScriptDetailScreen(
    navController: NavController,
    detailViewModel: ProjectDetailViewModel = hiltViewModel(),
    scriptViewModel: ProjectScriptViewModel = hiltViewModel(),
    onEdit: (ProjectScriptViewModel) -> Unit = {}
) {
    val detailState by detailViewModel.state.collectAsState()
    val scriptState by scriptViewModel.scriptState.collectAsState()

    LaunchedEffect(detailState.project) {
        detailState.project?.let {
            scriptViewModel.initializeScript(
                projectId = it.hashCode().toLong(),
                title = it.name,
                rawScript = it.script
            )
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
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
                        onClick = { onEdit(scriptViewModel) }
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
                text = scriptState.title,
                style = Typography.displayMedium.copy(TextPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            )

            if (scriptState.isLoading) {
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
                    if (scriptState.paragraphs.isEmpty()) {
                        item {
                            EmptyStateView(
                                imageRes = R.drawable.character_home_1,
                                message = "등록된 대본이 없어요!",
                                modifier = Modifier.padding(innerPadding),
                                backgroundColor = White
                            )
                        }
                    } else {
                        items(scriptState.paragraphs) { paragraph ->
                            Text(
                                text = paragraph.text,
                                style = Typography.bodyLarge.copy(TextPrimary),
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

