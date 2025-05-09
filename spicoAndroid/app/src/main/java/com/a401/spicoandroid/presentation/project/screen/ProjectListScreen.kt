package com.a401.spicoandroid.presentation.project.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.CommonList
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.component.IconButton
import com.a401.spicoandroid.common.ui.theme.BrokenWhite
import com.a401.spicoandroid.domain.project.model.ProjectScreenType
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectViewModel

@Composable
fun ProjectListScreen(
    navController: NavController,
    projectViewModel: ProjectViewModel,
    onFabClick: () -> Unit
) {
    val projectListState by projectViewModel.projectListState.collectAsState()

    LaunchedEffect(Unit) {
        projectViewModel.fetchProjects(size = 10, cursor = null, screenType = ProjectScreenType.LIST)
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 목록",
                rightContent = {
                    IconButton(
                        iconResId = R.drawable.ic_add_black,
                        contentDescription = "프로젝트 추가",
                        onClick = {}
                    )
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
            projectListState.projects.forEachIndexed { index, project ->
                CommonList(
                    imagePainter = painterResource(R.drawable.img_create_project),
                    title = project.title,
                    description = project.date.toString(),
                    rightIcon = painterResource(R.drawable.ic_arrow_right_balck),
                    onClick = {
                        navController.navigate(NavRoutes.ProjectDetail.withId(project.id))
                    }
                )
                if (index != projectListState.projects.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProjectListScreenPreview() {
//    ProjectListScreen(
//        navController = rememberNavController(),
//        onFabClick = {}
//    )
//}
