package com.a401.spicoandroid.presentation.report.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext
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
    source: String = "script",
    viewModel: FinalReportViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(initialPage = 0) { state.reportItems.size }
    val coroutineScope = rememberCoroutineScope()
    var isAlertVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로 가기",
                        onClick = {
                            when (source) {
                                "home" -> {
                                    navController.navigate(NavRoutes.Home.route) {
                                        popUpTo(NavRoutes.Home.route) { inclusive = false }
                                    }
                                }
                                else -> {
                                    val targetRoute = NavRoutes.ProjectDetail.withId(projectId)
                                    navController.navigate(targetRoute) {
                                        popUpTo(NavRoutes.ProjectList.route) { inclusive = false }
                                    }
                                }
                            }
                        }
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        CommonButton(
                            text = "음성 스크립트",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate(
                                    NavRoutes.VoiceScript.withArgs(projectId, practiceId)
                                )
                            }
                        )

                        CommonButton(
                            text = "발표 영상 다시 보기",
                            onClick = {
                                val intent = Intent(Intent.ACTION_MAIN).apply {
                                    addCategory(Intent.CATEGORY_APP_GALLERY)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
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
                    Log.d("PracticeList", "🧨 삭제 버튼 클릭됨 (UI)")

                    viewModel.deleteReport(
                        projectId = projectId,
                        practiceId = practiceId,
                        onSuccess = {
                            Toast.makeText(context, "리포트를 삭제했어요", Toast.LENGTH_SHORT).show()
                            navController.navigate(NavRoutes.ProjectDetail.withId(projectId)) {
                                popUpTo(NavRoutes.ProjectList.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onError = { throwable ->
                            Toast.makeText(context, "삭제에 실패했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                            Log.e("FinalReport", "❌ 삭제 실패: ${throwable.message}", throwable)
                        }
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

