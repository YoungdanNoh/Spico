package com.a401.spicoandroid.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    onNextWeek: () -> Unit,
    onDateClick: (LocalDate) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("d")
    val weekdays = listOf("월", "화", "수", "목", "금", "토", "일")
    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .widthIn(max = 360.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 타이틀 + 좌우 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .width(328.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left_small_text_tertiary),
                contentDescription = "이전 주",
                tint = Color.Unspecified,
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
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onNextWeek() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 요일 + 점 + 날짜
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .widthIn(max = 360.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            currentWeekDates.forEachIndexed { index, date ->
                val isToday = date == today
                val isMarked = markedDates.contains(date)

                val baseModifier = Modifier
                    .width(40.dp)
                    .height(88.dp)

                val todayBoxModifier = baseModifier
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, LineTertiary, RoundedCornerShape(4.dp))

                val appliedModifier = if (isToday) todayBoxModifier else baseModifier

                Box(
                    modifier = appliedModifier
                        .clickable(enabled = isMarked) { onDateClick(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = weekdays[index],
                            style = Typography.titleMedium,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isMarked) {
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
}

