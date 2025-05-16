package com.a401.spicoandroid.presentation.practice.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.formatDateWithDay
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeMode
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel


@Composable
fun ProjectSelectScreen(
    navController: NavController,
    viewModel: PracticeViewModel = hiltViewModel()
) {

    val projectList by viewModel.projectList.collectAsState()
    val selectedMode = viewModel.selectedMode

    LaunchedEffect(Unit) {
        viewModel.fetchProjectList()
    }

    Log.d("PracticeDebug", "ProjectSelectScreen 진입 - selectedMode: $selectedMode")

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 선택",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        onClick = {
                            viewModel.reset() // 전체 흐름 초기화
                            navController.popBackStack()
                        }
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonIconTextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    iconResId = R.drawable.ic_add_white,
                    text = "새 프로젝트 생성",
                    size = ButtonSize.LG,
                    onClick = { navController.navigate(NavRoutes.ProjectCreate.withReset(true)) }
                )
            }
        },
        containerColor = BrokenWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            if (projectList.isEmpty()) {
                // 프로젝트 없음 화면
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.character_home_1),
                        contentDescription = "프로젝트 없음 이미지",
                        modifier = Modifier.size(140.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "프로젝트가 없어요.\n프로젝트를 생성해보세요!",
                        style = Typography.titleLarge.copy(lineHeight = 28.sp),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // 프로젝트 리스트
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    projectList.forEach { project ->
                        CommonList(
                            modifier = Modifier.dropShadow1(),
                            imagePainter = painterResource(id = R.drawable.img_list_practice),
                            title = project.title,
                            description = formatDateWithDay(project.date),
                            onClick = {
                                viewModel.selectedProject = project


                                when (selectedMode) {
                                    PracticeMode.FINAL -> {
                                        navController.navigate(NavRoutes.FinalSetting.route)
                                    }

                                    PracticeMode.COACHING -> {
                                        viewModel.createPractice(
                                            onSuccess = {
                                                navController.navigate(NavRoutes.CoachingMode.route)
                                            },
                                            onFailure = {
                                                Log.e("ProjectSelectScreen", "코칭 연습 생성 실패", it)
                                            }
                                        )
                                    }

                                    null -> {
                                        // TODO: 모드가 선택되지 않았을 경우 처리
                                    }
                                }
                            }

                        )
                    }
                }
            }
        }
    }
}

