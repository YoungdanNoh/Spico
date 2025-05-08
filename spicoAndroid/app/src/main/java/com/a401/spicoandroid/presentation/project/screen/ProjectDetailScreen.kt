package com.a401.spicoandroid.presentation.project.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.a401.spicoandroid.common.ui.component.IconButton
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.project.component.ProjectEditDialog
import com.a401.spicoandroid.presentation.project.component.ProjectInfoHeader
import com.a401.spicoandroid.presentation.project.viewmodel.Project
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectDetailScreen(
    project: Project
) {
    val practiceList = listOf(1, 2, 3)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(BrokenWhite)
        ) {
            ProjectInfoHeader(
                title = project.title,
                time = "15:00"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonReportTabBar(
                selectedTabIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            practiceList.forEachIndexed { index, round ->
                CommonList(
                    title = "코칭모드 ${round}회차",
                    description = project.title,
                    onLongClick = {
                        isBottomSheetVisible = true
                    }
                )
                if (index != practiceList.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProjectDetailScreenPreview() {
    val mockProject = Project(
        title = "자율 프로젝트",
        date = "2025.04.25. 금요일"
    )
    ProjectDetailScreen(
        project = mockProject
    )
}