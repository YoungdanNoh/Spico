package com.a401.spicoandroid.common.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.Hover
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.TextTertiary
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.theme.White
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomMonthCalendar(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val startDate = currentMonth.atDay(1)
    val endDate = currentMonth.atEndOfMonth()
    val firstDayOfWeek = DayOfWeek.SUNDAY

    val days = buildList<LocalDate?> {
        val dayShift = (startDate.dayOfWeek.value - firstDayOfWeek.value + 7) % 7

        val previousMonth = currentMonth.minusMonths(1)
        val lastDayOfPrevMonth = previousMonth.atEndOfMonth()
        for (i in dayShift downTo 1) {
            add(lastDayOfPrevMonth.minusDays((i - 1).toLong()))
        }

        var date = startDate
        while (!date.isAfter(endDate)) {
            add(date)
            date = date.plusDays(1)
        }

        while (size < 42) {
            add(date)
            date = date.plusDays(1)
        }
    }

    // 요일
    val daysOfWeek = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    Row(Modifier.fillMaxWidth()) {
        daysOfWeek.forEach { day ->
            Text(
                text = day.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                textAlign = TextAlign.Center,
                style = Typography.titleMedium,
                color = TextTertiary
            )
        }
    }


    // 날짜
    Column {
        days.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (day == selectedDate) Hover else Color.Transparent
                            )
                            .clickable(enabled = day != null) {
                                day?.let { onDateSelected(it) }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day?.dayOfMonth?.toString() ?: "",
                            style = Typography.titleMedium,
                            color = when {
                                day == selectedDate -> White
                                day == null || day.month != currentMonth.month -> TextTertiary
                                else -> TextPrimary
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomOverlayCalendar(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x29222222))
                .clickable(onClick = onDismiss)
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            currentMonth = currentMonth.minusMonths(1)
                            selectedDate = currentMonth.atDay(1)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_left_black), contentDescription = null, tint = TextPrimary)
                        }

                        Text("${currentMonth.year}년 ${currentMonth.monthValue}월", style = Typography.headlineLarge, color = TextPrimary)

                        IconButton(onClick = {
                            currentMonth = currentMonth.plusMonths(1)
                            selectedDate = currentMonth.atDay(1)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_right_black), contentDescription = null, tint = TextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomMonthCalendar(
                        currentMonth = currentMonth,
                        selectedDate = selectedDate,
                        onDateSelected = {
                            selectedDate = it
                            onDateSelected(it)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}



