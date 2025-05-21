package com.a401.spicoandroid.common.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.Placeholder
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.TextTertiary
import com.a401.spicoandroid.common.ui.theme.Typography
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCalendar by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("yyyy. M. d. EEEE", Locale.KOREAN)
    val displayedText = selectedDate?.format(formatter) ?: "날짜를 선택하세요"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Placeholder, RoundedCornerShape(8.dp))
            .clickable { showCalendar = true }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayedText,
                style = Typography.titleLarge,
                color = if (selectedDate == null) TextTertiary else TextPrimary
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar_text_tertiary),
                contentDescription = "달력 아이콘",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if (showCalendar) {
        CustomOverlayCalendar(
            initialDate = selectedDate ?: LocalDate.now(),
            onDismiss = { showCalendar = false },
            onDateSelected = {
                onDateSelected(it)
                showCalendar = false
            }
        )
    }
}