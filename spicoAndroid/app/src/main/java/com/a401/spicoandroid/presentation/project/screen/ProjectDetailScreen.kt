package com.a401.spicoandroid.presentation.project.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.CommonBottomBar
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.component.CommonReportTabBar
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.component.IconButton
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.project.component.ProjectInfoHeader
import com.a401.spicoandroid.presentation.project.viewmodel.Project

@Composable
fun ProjectDetailScreen(
    navController: NavController,
    onFabClick: () -> Unit,
    project: Project
) {

    val projectList = listOf(
        Project("자율 프로젝트", "2025.04.25. 금요일"),
        Project("특화 프로젝트", "2025.04.25. 금요일"),
        Project("공통 프로젝트", "2025.04.25. 금요일"),
        Project("관통 프로젝트", "2025.04.25. 금요일")
    )

    var selectedTab by remember { mutableStateOf(0) }

    val practiceList = listOf(1, 2, 3)

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
                    IconButton(
                        iconResId = R.drawable.ic_more_black,
                        contentDescription = "옵션",
                        onClick = {}
                    )
                }
            )
        },
        bottomBar = {
            CommonBottomBar(
                navController = navController,
                onFabClick = onFabClick
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

            practiceList.forEach { round ->
                CommonList(
                    title = "코칭모드 ${round}회차",
                    description = project.title
                )
                if (round != projectList.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectDetailScreenPreview() {
    val mockProject = Project(
        title = "자율 프로젝트",
        date = "2025.04.25. 금요일"
    )
    ProjectDetailScreen(
        navController = rememberNavController(),
        onFabClick = {},
        project = mockProject
    )
}
