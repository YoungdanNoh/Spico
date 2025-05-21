package com.a401.spicoandroid.presentation.project.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.common.ui.component.BackIconButton
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTextField
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.component.DatePicker
import com.a401.spicoandroid.common.ui.component.FlexibleTimePickerDialog
import com.a401.spicoandroid.common.ui.component.TimePicker
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.theme.White
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectFormViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectCreateScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ProjectFormViewModel = hiltViewModel(),
    reset: Boolean = false
){
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val today = remember { LocalDate.now() }

    val isFormValid = remember(state.projectName, state.projectDate, state.projectTime) {
        state.projectName.isNotBlank() &&
        state.projectDate?.let { !it.isBefore(today) } == true &&
        state.projectTime in 30..1800
    }

    LaunchedEffect(reset) {
        if (reset) {
            viewModel.resetForm()
        }
    }

    Scaffold (
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 생성",
                leftContent = { BackIconButton(navController) }
            )
        },
        bottomBar = {
            if (!isFocused.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CommonButton(
                        text = "다음",
                        size = ButtonSize.LG,
                        backgroundColor = Action,
                        borderColor = Action,
                        textColor = White,
                        onClick = {
                            if (isFormValid) {
                                navController.navigate("project_script_input")
                            } else {
                                Toast.makeText(context, "모든 항목을 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        },
        containerColor = White,
    ) {
        innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus(true) }
                }
        ) {
            Column {
                Text(
                    text = "프로젝트 명",
                    style = Typography.headlineLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                CommonTextField(
                    value = state.projectName,
                    onValueChange = viewModel::updateProjectName,
                    placeholder = "프로젝트명을 입력하세요.",
                    modifier = Modifier
                        .padding(bottom = 36.dp)
                        .onFocusChanged { focusState ->
                            isFocused.value = focusState.isFocused
                        }
                )
                Text(
                    text = "발표날짜",
                    style = Typography.headlineLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                DatePicker(
                    selectedDate = state.projectDate,
                    onDateSelected = {
                        focusManager.clearFocus(force = true)
                        viewModel.updateProjectDate(it)
                    },
                    modifier = Modifier.padding(bottom = 36.dp),
                )
                Text(
                    text = "발표시간",
                    style = Typography.headlineLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                TimePicker(
                    minute = state.projectTime % 60,
                    second = 0,
                    onTimeSelected = viewModel::updateProjectTime,
                    validate = { m, s ->
                        val totalSeconds = m * 60 + s
                        when {
                            totalSeconds < 30 -> "발표시간은 최소 30초 입니다."
                            totalSeconds > 1800 -> "발표시간은 최대 30분 입니다."
                            else -> null
                        }
                    },
                    guideText = "발표시간은 30초 ~ 30분 입니다.",
                    timePickerDialog = { show, onDismiss, onTimeSelected ->
                        if (show) {
                            focusManager.clearFocus(force = true)
                            FlexibleTimePickerDialog(
                                initialMinute = state.projectTime % 60,
                                initialSecond = 0,
                                onDismiss = onDismiss,
                                onTimeSelected = onTimeSelected
                            )
                        }
                    }
                )
            }
        }
    }
}
