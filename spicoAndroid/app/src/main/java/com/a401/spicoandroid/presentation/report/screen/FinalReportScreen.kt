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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.report.component.*
import com.a401.spicoandroid.presentation.report.viewmodel.FinalReportViewModel
import com.a401.spicoandroid.ui.component.TimeSegment
import kotlinx.coroutines.launch

data class ReportCategoryData(
    val title: String,
    val description: String,
    val iconResId: Int,
    val timeRangeText: String,
    val segments: List<TimeSegment>
)

@Composable
fun FinalReportScreen(
    viewModel: FinalReportViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val segments = listOf(TimeSegment(60000L, 90000L))

    val labels = listOf("발음", "속도", "성량", "휴지", "대본")

    val reportItems = listOf(
        ReportCategoryData("발음", "특정 구간에서 발음이 뭉개져요", R.drawable.img_feedback_pronunciation, "1:00 ~ 1:30", segments),
        ReportCategoryData("속도", "말의 속도가 느린 편이에요", R.drawable.img_feedback_speed, "1:30 ~ 2:00", segments),
        ReportCategoryData("휴지", "휴지 기간이 총 5회 있었어요", R.drawable.img_feedback_silence, "0:30 ~ 1:00", segments),
        ReportCategoryData("성량", "목소리 크기가 많이 작아요", R.drawable.img_feedback_volume, "0:30 ~ 1:00", segments),
        ReportCategoryData("대본일치도", "불일치 문장이 총 5회 있었어요", R.drawable.img_feedback_script_match, "0:30 ~ 1:00", segments)
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { reportItems.size }
    )

    val coroutineScope = rememberCoroutineScope()

    var isAlertVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "리포트",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.arrow_left,
                        contentDescription = "뒤로 가기",
                        onClick = {}
                    )
                },
                rightContent = {
                    CommonButton(
                        text = "삭제",
                        size = ButtonSize.XS,
                        backgroundColor = White,
                        textColor = Error,
                        borderColor = Error,
                        onClick = {
                            isAlertVisible = true
                        }
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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportInfoHeader(
                imagePainter = painterResource(id = R.drawable.img_final_practice),
                projectTitle = "자율 프로젝트",
                roundCount = 5,
                chipText = "파이널모드",
                chipType = ChipType.REPORT_ERROR
            )
            Column{
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
                        labels = labels,
                        scores = state.scores
                    )
                }

                Column{
                    HorizontalPager(
                        state = pagerState,
                        pageSize = PageSize.Fill,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) { page ->
                        val item = reportItems[page]
                        ReportCategoryCard(
                            title = item.title,
                            description = item.description,
                            iconResId = item.iconResId,
                            timeRangeText = item.timeRangeText,
                            totalStartMillis = 0L,
                            totalEndMillis = 180000L,
                            segments = item.segments
                        )
                    }

                    CommonCircularProgressBar(
                        selectedIndex = pagerState.currentPage,
                        totalCount = reportItems.size,
                        onSelect = { index ->
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            Text("Q&A", style = Typography.headlineLarge)
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.qnaList.forEachIndexed { index, (question, answer) ->
                    ReportQnAItem("Q${index + 1}. $question", answer)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CommonButton(
                    text = "음성 스크립트",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )

                CommonButton(
                    text = "발표 영상 다시 보기",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = White,
                    textColor = Action,
                    borderColor = Action
                )
            }
        }
        if (isAlertVisible) {
            CommonAlert(
                title = "리포트를 삭제하시겠습니까?",
                confirmText = "삭제",
                onConfirm = {
                    isAlertVisible = false
                },
                confirmTextColor = White,
                confirmBackgroundColor = Error,
                confirmBorderColor = Error,
                cancelText = "취소",
                onCancel = {
                    isAlertVisible = false
                },
                onDismissRequest = {
                    isAlertVisible = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinalReportScreenPreview() {
    FinalReportScreen()
}
