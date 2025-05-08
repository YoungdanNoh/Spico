package com.a401.spicoandroid.presentation.project.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.CommonTextField
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.component.IconButton
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.common.ui.theme.Typography
import com.a401.spicoandroid.common.ui.theme.White

@Composable
fun ProjectScriptInputScreen (
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    var script by remember { mutableStateOf("") }
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            CommonTopBar(
                centerText = "프로젝트 생성",
                leftContent = {
                    IconButton(
                        iconResId = R.drawable.ic_arrow_left_black,
                        contentDescription = "뒤로가기",
                        //TODO: 클릭시 이벤트 추가하기
                        onClick = {}
                    )
                }
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
                    detectTapGestures {
                        focusManager.clearFocus(true)
                    }
                }
        ) {
            Column(

            ) {
                Text(
                    text = "대본",
                    style = Typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                CommonTextField(
                    value = script,
                    onValueChange = { script = it },
                    placeholder = "대본을 입력하세요.",
                    modifier = Modifier.fillMaxHeight(1f)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 720,
    name = "ProjectScriptInputScreen Preview"
)
@Composable
fun PreviewProjectScriptInputScreen() {
    val navController = rememberNavController()

    SpeakoAndroidTheme {
        ProjectScriptInputScreen(navController = navController)
    }
}