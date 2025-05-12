package com.a401.spicoandroid.presentation.project.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectEditDialog(
    projectTitle: String,
    onTitleChange: (String) -> Unit,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    hour: Int,
    minute: Int,
    second: Int,
    onTimeSelected: (Int, Int, Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Popup (
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)) // 반투명 어두운 배경 (50% 투명도)
                .clickable(onClick = onDismiss)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .wrapContentSize()
                    .background(White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "프로젝트 명",
                        style = Typography.headlineLarge,
                    )
                    CommonTextField(
                        value = projectTitle,
                        onValueChange = onTitleChange,
                        placeholder = "프로젝트명을 입력하세요.",
                    )

                    Text(
                        text = "발표날짜",
                        style = Typography.headlineLarge,
                    )
                    DatePicker(
                        selectedDate = selectedDate,
                        onDateSelected = onDateSelected,
                    )

                    Text(
                        text = "제한시간",
                        style = Typography.headlineLarge,
                    )
                    TimePicker(
                        minute = minute,
                        second = second,
                        onTimeSelected = onTimeSelected,
                        validate = { m, s ->
                            val totalSeconds = m * 60 + s
                            when {
                                totalSeconds < 30 -> "발표시간은 최소 30초 입니다."
                                totalSeconds > 1800 -> "발표시간은 최대 30분 입니다."
                                else -> null
                            }
                        },
                        guideText = "발표시간은 30초 ~ 30분 입니다.",
                        timePickerDialog = { show, onDismiss, onTimeSelectedDialog ->
                            if (show) {
                                FlexibleTimePickerDialog(
                                    initialMinute = minute,
                                    initialSecond = second,
                                    onDismiss = onDismiss,
                                    onTimeSelected = onTimeSelectedDialog
                                )
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CommonButton(
                            text = "취소",
                            backgroundColor = BackgroundSecondary,
                            textColor = TextTertiary,
                            borderColor = BackgroundSecondary,
                            modifier = Modifier.weight(1f),
                            onClick = onDismiss
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        CommonButton(
                            text = "수정하기",
                            backgroundColor = Action,
                            textColor = White,
                            modifier = Modifier.weight(1f),
                            onClick = onConfirm
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    widthDp = 312,
    heightDp = 393,
    name = "ProjectEditDialog Preview"
)
@Composable
fun ProjectEditDialogPreview() {
    var title by remember { mutableStateOf("관통 프로젝트") }
    var date by remember { mutableStateOf<LocalDate?>(LocalDate.of(2025, 4, 25)) }
    var hour by remember { mutableIntStateOf(1) }
    var minute by remember { mutableIntStateOf(30) }
    var second by remember { mutableIntStateOf(0) }

    SpeakoAndroidTheme {
        ProjectEditDialog(
            projectTitle = title,
            onTitleChange = { title = it },
            selectedDate = date,
            onDateSelected = { date = it },
            hour = hour,
            minute = minute,
            second = second,
            onTimeSelected = { h, m, s ->
                hour = h
                minute = m
                second = s
            },
            onDismiss = {},
            onConfirm = {}
        )
    }
}
