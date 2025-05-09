package com.a401.spicoandroid.presentation.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.common.ui.theme.BackgroundPrimary
import com.a401.spicoandroid.presentation.home.component.GreetingSection
import com.a401.spicoandroid.presentation.home.component.HomeFooterSection
import com.a401.spicoandroid.presentation.home.component.PracticeSection
import com.a401.spicoandroid.presentation.home.component.RecentReportSection
import com.a401.spicoandroid.presentation.home.component.WeeklyCalendarSection
import com.a401.spicoandroid.presentation.home.dummy.DummyProjectList
import com.a401.spicoandroid.presentation.home.model.ProjectSchedule
import com.a401.spicoandroid.presentation.home.util.getStartOfWeek
import com.a401.spicoandroid.presentation.home.util.getWeekDates
import com.a401.spicoandroid.presentation.home.viewmodel.WeeklyCalendarViewModel
import java.time.LocalDate

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    calendarViewModel: WeeklyCalendarViewModel = hiltViewModel()
) {
    val currentWeekDates by calendarViewModel.currentWeekDates.collectAsState()
    val markedDates by calendarViewModel.markedDates.collectAsState()

    LaunchedEffect(Unit) {
        calendarViewModel.updateProjectList(DummyProjectList)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(BackgroundPrimary)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 상단 카드
        GreetingSection(navController = navController)

        // 주간 달력
        WeeklyCalendarSection(
            currentWeekDates = currentWeekDates,
            markedDates = markedDates,
            onPreviousWeek = { calendarViewModel.moveToPreviousWeek() },
            onNextWeek = { calendarViewModel.moveToNextWeek() }
        )

        // 모드 선택
        PracticeSection()
        // 최근 연습 리포트
        RecentReportSection()
        // 푸터
        HomeFooterSection()
    }
}

//@Preview(showBackground = true, widthDp = 360, heightDp = 1050)
//@Composable
//fun HomeScreenPreview() {
//    val fakeStartDate = getStartOfWeek(LocalDate.now())
//    val fakeWeekDates = getWeekDates(fakeStartDate)
//    val fakeMarkedDates = listOf(fakeWeekDates[2], fakeWeekDates[4])
//
//    val fakeNavController = rememberNavController()
//
//    Scaffold(
//        containerColor = BackgroundPrimary,
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(horizontal = 16.dp, vertical = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(24.dp)
//        ) {
//            GreetingSection()
//
//            WeeklyCalendarSection(
//                currentWeekDates = fakeWeekDates,
//                markedDates = fakeMarkedDates,
//                onPreviousWeek = {},
//                onNextWeek = {}
//            )
//
//            PracticeSection()
//            RecentReportSection()
//        }
//    }
//}
//
//
