package com.a401.spicoandroid.presentation.project.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.bottomsheet.DeleteModalBottomSheet
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.error.screen.NotFoundScreen
import com.a401.spicoandroid.presentation.project.component.ProjectEditDialog
import com.a401.spicoandroid.presentation.project.component.ProjectInfoHeader
import com.a401.spicoandroid.presentation.project.viewmodel.PracticeViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectDetailViewModel
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import java.time.LocalDate
import android.util.Log

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    viewModel: ProjectDetailViewModel = hiltViewModel(),
    projectId: Int
) {
    var selectedTab by remember { mutableStateOf(0) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var showDeleteAlert by remember { mutableStateOf(false) }
    var isEditDialogVisible by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }
    var second by remember { mutableIntStateOf(0) }


    var tempTitle by remember { mutableStateOf("") }
    var tempDate by remember { mutableStateOf<LocalDate?>(null) }
    var tempMinute by remember { mutableIntStateOf(0) }
    var tempSecond by remember { mutableIntStateOf(0) }

    val state by viewModel.state.collectAsState()
    val isLoading = state.isLoading
    val error = state.error
    val project = state.project

    val practiceViewModel: PracticeViewModel = hiltViewModel()
    val practiceState by practiceViewModel.practiceListState.collectAsState()
    val practiceDeleteState by practiceViewModel.practiceDeleteState.collectAsState()

    val projectViewModel: ProjectViewModel = hiltViewModel()
    val deleteState by projectViewModel.deleteState.collectAsState()

    var selectedPracticeId by remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) {
        viewModel.fetchProjectDetail(projectId)
    }

    LaunchedEffect(selectedTab) {
        val filter = when (selectedTab) {
            1 -> "final"
            2 -> "coaching"
            else -> null // 0번: 전체
        }

        Log.d("PracticeList", "📥 테스트용 연습 목록 불러오기: projectId=$projectId, filter=final")

        practiceViewModel.fetchPracticeList(
            projectId = projectId,
            filter = filter,
            cursor = null,
            size = 10
        )
    }

    LaunchedEffect(practiceDeleteState.isSuccess) {
        if (practiceDeleteState.isSuccess) {
            practiceViewModel.fetchPracticeList(
                projectId = projectId,
                filter = if (selectedTab == 0) "coaching" else "final",
                cursor = null,
                size = 10
            )
        }
    }

    LaunchedEffect(project) {
        project?.let {
            title = it.name
            selectedDate = LocalDate.parse(it.date)
            hour = it.time / 3600
            minute = (it.time % 3600) / 60
            second = it.time % 60
        }
    }


    val dropdownItems = listOf(
        DropdownMenuItemData(
            text = "수정하기",
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit_black),
                    contentDescription = "Edit",
                    tint = TextPrimary
                )
            }, onClick = {
                isDropdownExpanded = false
                tempTitle = title
                tempDate = selectedDate
                tempMinute = hour * 60 + minute
                tempSecond = second
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
                if (selectedPracticeId != -1) {
                    practiceViewModel.deletePractice(projectId, selectedPracticeId)
                }
                practiceViewModel.resetDeleteState()
            },
            onDismissRequest = {
                isBottomSheetVisible = false
            }
        )
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 상세",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        onClick = {
                            navController.popBackStack()
                        }
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
                    NotFoundScreen(navController = navController)
                }

                project != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        ProjectInfoHeader(
                            title = project.name,
                            time = formatTimeFromSeconds(project.time),
                            onScriptClick = { navController.navigate("script_detail") }
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
                                    onClick = {
                                        if (selectedTab == 1) { // 파이널 모드 리포트
                                            navController.navigate(
                                                NavRoutes.FinalReport.createRoute(
                                                    projectId = projectId,
                                                    practiceId = practice.id
                                                )
                                            )
                                        }
                                    },
                                    onLongClick = {
                                        selectedPracticeId = practice.id
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
            projectTitle = tempTitle,
            onTitleChange = { tempTitle = it },
            selectedDate = tempDate,
            onDateSelected = { tempDate = it },
            minute = tempMinute,
            second = tempSecond,
            onTimeSelected = { _, m, s ->
                tempMinute = m
                tempSecond = s
            },
            onDismiss = { isEditDialogVisible = false },
            onConfirm = {
                isEditDialogVisible = false

                val totalTime = tempMinute * 60 + tempSecond
                val dateStr = tempDate?.toString()

                projectViewModel.updateProject(
                    projectId = projectId,
                    name = title,
                    date = dateStr,
                    time = totalTime,
                    onSuccess = {
                        viewModel.fetchProjectDetail(projectId)
                    },
                    onError = { /* TODO */ }
                )
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
                navController.navigate(NavRoutes.ProjectList.route) // 바로 이동
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

fun formatTimeFromSeconds(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
