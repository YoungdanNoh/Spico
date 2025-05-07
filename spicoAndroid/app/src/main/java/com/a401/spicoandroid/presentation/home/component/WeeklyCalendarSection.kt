package com.a401.spicoandroid.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.home.util.getStartOfWeek
import com.a401.spicoandroid.presentation.home.util.getWeekDates
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WeeklyCalendarSection(
    currentWeekDates: List<LocalDate>,
    markedDates: List<LocalDate> = emptyList(),
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("d")
    val weekdays = listOf("월", "화", "수", "목", "금", "토", "일")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .widthIn(max = 360.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 타이틀 + 좌우 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .width(328.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left_small_text_tertiary),
                contentDescription = "이전 주",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onPreviousWeek() }
            )
            Text(
                text = "나의 발표 일정",
                style = Typography.headlineLarge,
                color = TextPrimary
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_small_text_tertiary),
                contentDescription = "다음 주",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onNextWeek() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 요일 + 점 + 날짜
        Row(
            modifier = Modifier.width(328.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            currentWeekDates.forEachIndexed { index, date ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = weekdays[index],
                        style = Typography.titleMedium,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (markedDates.contains(date)) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Action)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = date.format(dateFormatter),
                        style = Typography.titleLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun WeeklyCalendarSectionPreview() {
    val today = LocalDate.now()
    val startOfWeek = getStartOfWeek(today)
    val currentWeekDates = getWeekDates(startOfWeek)

    WeeklyCalendarSection(
        currentWeekDates = currentWeekDates,
        markedDates = listOf(currentWeekDates[2], currentWeekDates[5]),
        onPreviousWeek = {},
        onNextWeek = {}
    )
}
