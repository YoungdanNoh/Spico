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
import com.a401.spicoandroid.presentation.home.component.*
import com.a401.spicoandroid.presentation.home.dummy.DummyProjectList
import com.a401.spicoandroid.presentation.home.viewmodel.WeeklyCalendarViewModel
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel
import com.a401.spicoandroid.presentation.home.component.ProjectInfoDialog
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    calendarViewModel: WeeklyCalendarViewModel = hiltViewModel(),
    practiceViewModel: PracticeViewModel
) {
    val currentWeekDates by calendarViewModel.currentWeekDates.collectAsState()
    val markedDates by calendarViewModel.markedDates.collectAsState()

    // 클릭된 날짜를 저장
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(Unit) {
        calendarViewModel.updateProjectList(DummyProjectList)
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
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
        )

        // 하단 푸터
        HomeFooterSection(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        )
        Spacer(modifier = Modifier.height(36.dp))
    }

    // 일정이 있는 날짜 클릭 시만 다이얼로그 표시
    selectedDate.value?.let { clickedDate ->
        val projectListForDate = DummyProjectList.filter {
            it.projectLocalDate == clickedDate
        }

        if (projectListForDate.isNotEmpty()) {
            ProjectInfoDialog(
                dateTitle = "${clickedDate.monthValue}월 ${clickedDate.dayOfMonth}일",
                projectList = projectListForDate,
                navController = navController,
                onDismiss = { selectedDate.value = null }
            )
        } else {
            selectedDate.value = null
        }
    }
}
