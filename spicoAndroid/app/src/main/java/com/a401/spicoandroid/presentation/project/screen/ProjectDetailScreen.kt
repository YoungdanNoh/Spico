package com.a401.spicoandroid.presentation.project.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.bottomsheet.DeleteModalBottomSheet
import com.a401.spicoandroid.common.ui.component.CommonAlert
import com.a401.spicoandroid.common.ui.component.CommonDropdown
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.component.CommonReportTabBar
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.component.DropdownMenuItemData
import com.a401.spicoandroid.common.ui.component.EmptyStateView
import com.a401.spicoandroid.common.ui.component.IconButton
import com.a401.spicoandroid.common.ui.component.LoadingInProgressView
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.domain.project.model.Project
import com.a401.spicoandroid.presentation.project.component.ProjectEditDialog
import com.a401.spicoandroid.presentation.project.component.ProjectInfoHeader
import com.a401.spicoandroid.presentation.project.viewmodel.PracticeViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectDetailViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    projectId: Int
) {
    var selectedTab by remember { mutableStateOf(0) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var showDeleteAlert by remember { mutableStateOf(false) }
    var isEditDialogVisible by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("프로젝트 제목") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }
    var second by remember { mutableIntStateOf(0) }

    val viewModel: ProjectDetailViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val isLoading = state.isLoading
    val error = state.error
    val project = state.project

    val practiceViewModel: PracticeViewModel = hiltViewModel()
    val practiceState by practiceViewModel.practiceListState.collectAsState()

    val projectViewModel: ProjectViewModel = hiltViewModel()
    val deleteState by projectViewModel.deleteState.collectAsState()

    // 프로젝트 정보는 최초 1회만 호출
    LaunchedEffect(Unit) {
        viewModel.fetchProjectDetail(projectId)
    }

    // 탭 선택이 변경될 때마다 연습 목록 다시 불러오기
    LaunchedEffect(selectedTab) {
        val filter = if (selectedTab == 0) "coaching" else "final"
        practiceViewModel.fetchPracticeList(
            projectId = projectId,
            filter = filter,
            cursor = null,
            size = 10
        )
    }

    LaunchedEffect(deleteState.isSuccess) {
        if (deleteState.isSuccess) {
            navController.popBackStack()
        }
    }


    val dropdownItems = listOf(
        DropdownMenuItemData(
            text = "수정하기",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_black),
                    contentDescription = "Edit",
                    tint = TextPrimary
                )
            }, onClick = {
                isDropdownExpanded = false
                isEditDialogVisible = true
            }
        ),
        DropdownMenuItemData(
            text = "프로젝트 삭제",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_trash_error),
                    contentDescription = "Delete",
                    tint = Error
                )
            },
            textColor = Error,
            onClick = {
                isDropdownExpanded = false
                showDeleteAlert = true
            }
        ),
    )

    if (isBottomSheetVisible) {
        DeleteModalBottomSheet(
            onDeleteClick = {
                isBottomSheetVisible = false
            },
            onDismissRequest = {
                isBottomSheetVisible = false
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 상세",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        onClick = {}
                    )
                },
                rightContent = {
                    Box {
                        IconButton(
                            iconResId = R.drawable.ic_more_black,
                            contentDescription = "옵션",
                            onClick = { isDropdownExpanded = true }
                        )

                        CommonDropdown(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            menuItems = dropdownItems
                        )
                    }
                }
            )
        },
        containerColor = BrokenWhite
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BrokenWhite)
        ) {
            when {
                isLoading -> {
                    LoadingInProgressView(
                        imageRes = R.drawable.character_home_5,
                        message = "프로젝트 정보를 불러오고 있어요.\n잠시만 기다려주세요!"
                    )
                }

                error != null -> {
                    Text(
                        text = "오류 발생: ${error.message ?: "알 수 없는 오류"}",
                        color = Error,
                    )
                }

                project != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        ProjectInfoHeader(
                            title = project.name,
                            time = "${project.time}:00"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CommonReportTabBar(
                            selectedTabIndex = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (practiceState.practices.isEmpty()) {
                            EmptyStateView(
                                imageRes = R.drawable.character_home_1,
                                message = "등록된 연습이 없어요.\n연습을 시작해보세요!",
                            )
                        } else {
                            practiceState.practices.forEachIndexed { index, practice ->
                                CommonList(
                                    title = "${practice.name} ${practice.count}회차",
                                    description = practice.createdAt,
                                    onLongClick = {
                                        isBottomSheetVisible = true
                                    }
                                )
                                if (index != practiceState.practices.lastIndex) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }


        if (isEditDialogVisible) {
        ProjectEditDialog(
            projectTitle = title,
            onTitleChange = { title = it },
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            hour = hour,
            minute = minute,
            second = second,
            onTimeSelected = { h, m, s ->
                hour = h
                minute = m
                second = s
            },
            onDismiss = { isEditDialogVisible = false },
            onConfirm = {
                isEditDialogVisible = false
            }
        )
    }

    if (showDeleteAlert) {
        CommonAlert(
            title = "프로젝트를 삭제하시겠습니까?",
            confirmText = "삭제",
            onConfirm = {
                showDeleteAlert = false
                projectViewModel.deleteProject(projectId)
            },
            cancelText = "취소",
            onCancel = {
                showDeleteAlert = false
            },
            onDismissRequest = {
                showDeleteAlert = false
            }
        )
    }
}