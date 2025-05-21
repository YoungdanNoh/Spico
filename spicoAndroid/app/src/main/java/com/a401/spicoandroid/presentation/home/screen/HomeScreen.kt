package com.a401.spicoandroid.presentation.home.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.formatMonthDay
import com.a401.spicoandroid.domain.home.model.PracticeType
import com.a401.spicoandroid.presentation.home.component.*
import com.a401.spicoandroid.presentation.home.viewmodel.WeeklyCalendarViewModel
import com.a401.spicoandroid.presentation.home.component.ProjectInfoDialog
import com.a401.spicoandroid.presentation.home.viewmodel.HomeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    calendarViewModel: WeeklyCalendarViewModel = hiltViewModel(),
    practiceViewModel: PracticeViewModel,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    // 닉네임
    val nickname by homeViewModel.nickname.collectAsState()
    val userNameToShow = nickname ?: "사용자"
    // 주간 달력
    val currentWeekDates by calendarViewModel.currentWeekDates.collectAsState()
    val markedDates by calendarViewModel.markedDates.collectAsState()

    // 클릭된 날짜를 저장
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    // 최근 리포트
    val recentReports by homeViewModel.recentReports.collectAsState()

    LaunchedEffect(Unit) {
        calendarViewModel.fetchCalendarProjects()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(BrokenWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // 인사말 카드
        GreetingSection(
            navController = navController,
            username = userNameToShow,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 24.dp)
        )

        // 주간 달력 + 모드 선택
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                WeeklyCalendarSection(
                    currentWeekDates = currentWeekDates,
                    markedDates = markedDates,
                    onPreviousWeek = { calendarViewModel.moveToPreviousWeek() },
                    onNextWeek = { calendarViewModel.moveToNextWeek() },
                    onDateClick = { clickedDate ->
                        selectedDate.value = clickedDate
                    }
                )

                PracticeSection(
                    navController = navController,
                    viewModel = practiceViewModel)
            }
        }

        // 최근 리포트
        RecentReportSection(
            reportList = recentReports,
            onReportClick = { report ->
                when (report.type) {
                    PracticeType.COACHING -> {
                        navController.navigate(
                            NavRoutes.HomeCoachingReportDetail.createRoute(
                                projectId = report.projectId,
                                practiceId = report.practiceId,
                                source = "home"
                            )
                        )
                    }
                    PracticeType.FINAL -> {
                        navController.navigate(
                            NavRoutes.HomeFinalReportDetail.createRoute(
                                projectId = report.projectId,
                                practiceId = report.practiceId,
                                source = "home"
                            )
                        )
                    }
                }
            }
        )
        // 하단 푸터
        HomeFooterSection(
            modifier = Modifier.padding(start = 24.dp, end = 20.dp, bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(36.dp))
    }

    // 일정이 있는 날짜 클릭 시만 다이얼로그 표시
    selectedDate.value?.let { clickedDate ->
        val project = calendarViewModel.findProjectByDate(clickedDate)

        if (project != null) {
            ProjectInfoDialog(
                dateTitle = formatMonthDay(clickedDate),
                projectList = listOf(project),
                navController = navController,
                onDismiss = { selectedDate.value = null }
            )
        } else {
            selectedDate.value = null
        }

    }
}
