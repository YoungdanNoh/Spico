package com.a401.spicoandroid.presentation.project.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
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
import com.a401.spicoandroid.common.ui.component.TimePicker
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.theme.White
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectFormViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectSettingScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ProjectFormViewModel = hiltViewModel()
){
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()

    Scaffold (
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 생성",
                leftContent = { BackIconButton(navController) }
            )
        },
        bottomBar = {
            if (!isFocused.value) {
                CommonButton(
                    text = "다음",
                    size = ButtonSize.LG,
                    backgroundColor = Action,
                    borderColor = Action,
                    textColor = White,
                    onClick = { navController.navigate("project_script_input") },
                    modifier = Modifier.padding(16.dp)
                )
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
                    onDateSelected = viewModel::updateProjectDate,
                    modifier = Modifier.padding(bottom = 36.dp),
                )
                Text(
                    text = "제한시간",
                    style = Typography.headlineLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                TimePicker(
                    hour = state.projectTime / 60,
                    minute = state.projectTime % 60,
                    second = 0,
                    onTimeSelected = viewModel::updateProjectTime,
                )
            }
        }
    }
}
