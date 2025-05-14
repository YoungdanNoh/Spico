package com.a401.spicoandroid.presentation.project.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.a401.spicoandroid.common.ui.theme.Action
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.theme.White
import com.a401.spicoandroid.presentation.project.viewmodel.ProjectFormViewModel

@Composable
fun ProjectScriptInputScreen (
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ProjectFormViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val state by viewModel.state.collectAsState()

    Scaffold (
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 생성",
                leftContent = { BackIconButton(navController) }
            )
        },
        bottomBar = {
            CommonButton(
                text = "완료",
                size = ButtonSize.LG,
                backgroundColor = Action,
                borderColor = Action,
                textColor = White,
                onClick = {
                    viewModel.createProject(
                        onSuccess = { navController.navigate("project_list") },
                        onError = { navController.navigate("project_list") }
                    )
                },
                modifier = modifier.padding(16.dp)
            )
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
                    text = "대본",
                    style = Typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 12.dp),
                    color = TextPrimary
                )
                CommonTextField(
                    value = state.script,
                    onValueChange = viewModel::updateScript,
                    placeholder = "대본을 입력하세요.",
                    modifier = Modifier.fillMaxHeight(1f)
                )
            }
        }
    }
}
