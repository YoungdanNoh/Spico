package com.a401.spicoandroid.presentation.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.common.ui.component.CommonBottomBar
import com.a401.spicoandroid.common.ui.theme.BackgroundPrimary
import com.a401.spicoandroid.presentation.home.component.*
import com.a401.spicoandroid.presentation.home.util.getStartOfWeek
import com.a401.spicoandroid.presentation.home.util.getWeekDates
import com.a401.spicoandroid.presentation.home.viewmodel.WeeklyCalendarViewModel
import java.time.LocalDate

@Composable
fun HomeScreen() {
    val calendarViewModel = viewModel<WeeklyCalendarViewModel>()
    val currentWeekDates = calendarViewModel.currentWeekDates.collectAsState().value
    val markedDates = calendarViewModel.getMarkedDates()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(BackgroundPrimary)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        GreetingSection()

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

@Preview(showBackground = true, widthDp = 360, heightDp = 1050)
@Composable
fun HomeScreenPreview() {
    val fakeStartDate = getStartOfWeek(LocalDate.now())
    val fakeWeekDates = getWeekDates(fakeStartDate)
    val fakeMarkedDates = listOf(fakeWeekDates[2], fakeWeekDates[4])

    val fakeNavController = rememberNavController()

    Scaffold(
        containerColor = BackgroundPrimary,
        bottomBar = {
            CommonBottomBar(
                navController = fakeNavController,
                onFabClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            GreetingSection()

            WeeklyCalendarSection(
                currentWeekDates = fakeWeekDates,
                markedDates = fakeMarkedDates,
                onPreviousWeek = {},
                onNextWeek = {}
            )

            PracticeSection()
            RecentReportSection()
        }
    }
}


