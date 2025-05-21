package com.a401.spicoandroid.presentation.project.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
    minute: Int,
    second: Int,
    onTimeSelected: (Int, Int, Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val dummyFocusRequester = remember { FocusRequester() }

    Popup (
        alignment = Alignment.Center,
        onDismissRequest = {
            onDismiss()
        },
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
                .pointerInput(Unit) {
                    detectTapGestures {
                        onDismiss() // 외부 클릭 시 닫기
                    }
                }
                .padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .wrapContentSize()
                    .background(White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize() // 전체 덮기
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            dummyFocusRequester.requestFocus()
                        }
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "프로젝트 명",
                        style = Typography.headlineLarge,
                        color = TextPrimary
                    )
                    CommonTextField(
                        value = projectTitle,
                        onValueChange = onTitleChange,
                        placeholder = "프로젝트명을 입력하세요.",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "발표날짜",
                        style = Typography.headlineLarge,
                        color = TextPrimary,
                    )
                    DatePicker(
                        selectedDate = selectedDate,
                        onDateSelected = onDateSelected,
                    )

                    Text(
                        text = "발표시간",
                        style = Typography.headlineLarge,
                        color = TextPrimary,
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
                            onClick = {
                                onDismiss()
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        CommonButton(
                            text = "수정하기",
                            backgroundColor = Action,
                            textColor = White,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onConfirm()
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(1.dp)
                            .focusRequester(dummyFocusRequester)
                            .focusable()
                    )
                }
            }
        }
    }
}
