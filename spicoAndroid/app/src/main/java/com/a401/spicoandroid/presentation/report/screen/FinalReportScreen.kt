package com.a401.spicoandroid.presentation.report.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.error.screen.NotFoundScreen
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.report.component.*
import com.a401.spicoandroid.presentation.report.viewmodel.FinalReportViewModel
import kotlinx.coroutines.launch

@Composable
fun FinalReportScreen(
    navController: NavController,
    projectId: Int,
    practiceId: Int,
    viewModel: FinalReportViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(initialPage = 0) { state.reportItems.size }
    val coroutineScope = rememberCoroutineScope()
    var isAlertVisible by remember { mutableStateOf(false) }

    // API 호출
    LaunchedEffect(projectId, practiceId) {
        viewModel.fetchFinalReport(projectId, practiceId)
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "리포트",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.arrow_left,
                        contentDescription = "뒤로 가기",
                        onClick = { navController.popBackStack() }
                    )
                },
                rightContent = {
                    CommonButton(
                        text = "삭제",
                        size = ButtonSize.XS,
                        backgroundColor = White,
                        textColor = Error,
                        borderColor = Error,
                        onClick = { isAlertVisible = true }
                    )
                }
            )
        },
        containerColor = BrokenWhite
    ) { innerPadding ->

        when {
            state.isLoading -> {
                // 로딩 상태
                LoadingInProgressView(
                    imageRes = R.drawable.character_home_5,
                    message = "리포트를 불러오고 있어요\n잠시만 기다려주세요!"
                )
            }

            state.error != null -> {
                // 에러 상태
                NotFoundScreen(navController = navController)
            }

            else -> {
                // 정상 데이터 표시
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .background(BrokenWhite)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ReportInfoHeader(
                        imagePainter = painterResource(id = R.drawable.img_final_practice),
                        projectTitle = state.projectName,
                        roundCount = state.roundCount,
                        chipText = state.modeType,
                        chipType = ChipType.REPORT_ERROR
                    )

                    FinalScoreCard(
                        modeType = state.modeType,
                        roundCount = state.roundCount,
                        score = state.score
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        FinalRadarChart(
                            modifier = Modifier
                                .size(300.dp)
                                .padding(top = 24.dp)
                                .align(Alignment.Center),
                            labels = listOf("발음", "속도", "성량", "휴지", "대본"),
                            scores = state.scores
                        )
                    }

                    Column {
                        HorizontalPager(
                            state = pagerState,
                            pageSize = PageSize.Fill,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) { page ->
                            val item = state.reportItems[page]
                            ReportCategoryCard(
                                title = item.title,
                                description = item.description,
                                iconResId = item.iconResId,
                                timeRangeText = item.timeRangeText,
                                totalStartMillis = item.totalStartMillis,
                                totalEndMillis = item.totalEndMillis,
                                segments = item.segments,
                                progress = item.progress
                            )
                        }

                        CommonCircularProgressBar(
                            selectedIndex = pagerState.currentPage,
                            totalCount = state.reportItems.size,
                            onSelect = { index ->
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    }

                    Text("Q&A", style = Typography.headlineLarge)
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        state.qnaList.forEachIndexed { index, (question, answer) ->
                            ReportQnAItem("Q${index + 1}. $question", answer)
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        CommonButton(
                            text = "음성 스크립트",
                            onClick = {
                                navController.navigate(
                                    NavRoutes.VoiceScript.withArgs(projectId, practiceId)
                                )
                            }
                        )
                        CommonButton(
                            text = "발표 영상 다시 보기",
                            onClick = {
                                state.videoUrl?.let { url ->
                                    navController.navigate(NavRoutes.VideoReplay.withEncodedUrl(url))
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = White,
                            textColor = Action,
                            borderColor = Action
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }

        if (isAlertVisible) {
            CommonAlert(
                title = "리포트를 삭제하시겠습니까?",
                confirmText = "삭제",
                onConfirm = {
                    isAlertVisible = false
                    viewModel.deleteReport(
                        projectId = projectId,
                        practiceId = practiceId,
                        onSuccess = { navController.popBackStack(NavRoutes.ProjectList.route, false)},
                        onError = {}
                    )
                },
                confirmTextColor = White,
                confirmBackgroundColor = Error,
                confirmBorderColor = Error,
                cancelText = "취소",
                onCancel = { isAlertVisible = false },
                onDismissRequest = { isAlertVisible = false }
            )
        }
    }
}

