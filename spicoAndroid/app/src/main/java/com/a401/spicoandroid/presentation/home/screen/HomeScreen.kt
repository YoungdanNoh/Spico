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
import com.a401.spicoandroid.presentation.home.component.PracticeSection
import com.a401.spicoandroid.presentation.home.component.RecentReportSection
import com.a401.spicoandroid.presentation.home.component.WeeklyCalendarSection
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

    val dummyProjectList = listOf(
        ProjectSchedule(
            projectId = 1,
            projectName = "UX 리서치 회의",
            projectDate = "2025-05-06 10:00"
        ),
        ProjectSchedule(
            projectId = 2,
            projectName = "개발자 코드 리뷰",
            projectDate = "2025-05-08 14:00"
        ),
        ProjectSchedule(
            projectId = 3,
            projectName = "기획안 발표",
            projectDate = "2025-05-10 16:00"
        )
    )

    LaunchedEffect(Unit) {
        calendarViewModel.updateProjectList(dummyProjectList)
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
        GreetingSection(navController = navController)

        WeeklyCalendarSection(
            currentWeekDates = currentWeekDates,
            markedDates = markedDates,
            onPreviousWeek = { calendarViewModel.moveToPreviousWeek() },
            onNextWeek = { calendarViewModel.moveToNextWeek() }
        )

        PracticeSection()
        RecentReportSection()
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
