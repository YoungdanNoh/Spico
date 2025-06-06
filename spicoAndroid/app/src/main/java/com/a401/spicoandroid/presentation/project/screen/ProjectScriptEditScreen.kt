package com.a401.spicoandroid.presentation.project.screen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectScriptViewModel
import org.burnoutcrew.reorderable.*
import com.a401.spicoandroid.R
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectDetailViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectViewModel

@Composable
fun ProjectScriptEditScreen(
    navController : NavController,
    detailViewModel: ProjectDetailViewModel = hiltViewModel(),
    scriptViewModel: ProjectScriptViewModel = hiltViewModel(),
    projectViewModel: ProjectViewModel = hiltViewModel()
) {
    val scriptState by scriptViewModel.scriptState.collectAsState()
    val detailState by detailViewModel.state.collectAsState()

    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to -> scriptViewModel.moveParagraph(from.index, to.index) },
        onDragEnd = { _, _ -> }
    )

    val swipedItemId = remember { mutableStateOf<Long?>(null) }

    val focusManager = LocalFocusManager.current

    Scaffold(
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
                        text = "완료",
                        backgroundColor = Action,
                        borderColor = Action,
                        textColor = White,
                        size = ButtonSize.XS,
                        onClick = {
                            val scriptText = scriptViewModel.getMergedScript()
                            Log.d("SCRIPT_TEXT", scriptText)
                            projectViewModel.updateProject(
                                projectId = detailState.id,
                                script = scriptText,
                                onSuccess = {
                                    detailViewModel.fetchProjectDetail(detailState.id)
                                    navController.navigateUp()
                                },
                                onError = { }
                            )
                        }
                    )
                }
            )
        },
        containerColor = White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                        scriptViewModel.setEditing(false)
                    }
                }
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                state = reorderState.listState,
                modifier = Modifier
                    .fillMaxSize()
                    .reorderable(reorderState)
                    .detectReorderAfterLongPress(reorderState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(scriptState.paragraphs, key = { it.id }) { item ->
                    ReorderableItem(reorderState, key = item.id) { dragging ->
                        val isSwiped = swipedItemId.value == item.id
                        val offsetX by animateFloatAsState(if (isSwiped) -160f else 0f)
                        val scale by animateFloatAsState(if (dragging) 0.95f else 1f)

                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (isSwiped) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    CommonButton(
                                        text = "삭제",
                                        backgroundColor = White,
                                        borderColor = Error,
                                        textColor = Error,
                                        size = ButtonSize.XS,
                                        onClick = {
                                            scriptViewModel.deleteParagraph(item.id)
                                            swipedItemId.value = null
                                        }
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        translationX = offsetX
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .pointerInput(Unit) {
                                        detectHorizontalDragGestures(
                                            onHorizontalDrag = { change, dragAmount ->
                                                change.consume()
                                                if (dragAmount < -30) {
                                                    swipedItemId.value = item.id
                                                } else if (dragAmount > 30 && swipedItemId.value == item.id) {
                                                    swipedItemId.value = null
                                                }
                                            }
                                        )
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "문단 ${scriptState.paragraphs.indexOf(item) + 1}",
                                        style = Typography.displaySmall.copy(TextPrimary)
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_drag),
                                        contentDescription = "드래그 아이콘",
                                        tint = TextSecondary
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                CommonTextField(
                                    value = item.text,
                                    onValueChange = { scriptViewModel.updateText(item.id, it) },
                                    placeholder = "내용을 입력하세요",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 120.dp)
                                        .onFocusChanged { scriptViewModel.setEditing(it.isFocused) }
                                )
                            }
                        }
                    }

                }
                if (scriptState.paragraphs.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            EmptyStateView(
                                imageRes = R.drawable.character_home_1,
                                message = "아직 문단이 없어요.\n오른쪽 아래 + 버튼을 눌러 문단을 추가해보세요!",
                                modifier = Modifier.padding(innerPadding),
                                backgroundColor = White
                            )
                        }
                    }
                }
            }

            if (!scriptState.isEditing) {
                IconCircleButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_white),
                            contentDescription = "문단 추가",
                            tint = White
                        )
                    },
                    backgroundColor = Action
                ) {
                    scriptViewModel.addParagraph()
                }
            }
        }
    }
}
