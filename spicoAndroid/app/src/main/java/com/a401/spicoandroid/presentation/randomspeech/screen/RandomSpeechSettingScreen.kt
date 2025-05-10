package com.a401.spicoandroid.presentation.randomspeech.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.BackIconButton
import com.a401.spicoandroid.common.ui.component.ButtonSize
import com.a401.spicoandroid.common.ui.component.CommonButton
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.component.TimePicker
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.getTopicKor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RandomSpeechSettingScreen(
    topic: String,
    navController: NavController,
    onNext: (prepMin: Int, speakMin: Int) -> Unit
) {
    val topicKor = getTopicKor(topic)

    var prepMinute by remember { mutableIntStateOf(1) }
    var prepSecond by remember { mutableIntStateOf(0) }
    var speakMinute by remember { mutableIntStateOf(3) }
    var speakSecond by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "설정",
                leftContent = { BackIconButton(navController) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.character_random),
                        contentDescription = "캐릭터",
                        modifier = Modifier.height(108.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row {
                            Text("지금 선택한 주제는 ", style = Typography.headlineLarge, color = TextPrimary)
                            Text(topicKor, style = Typography.headlineLarge, color = Hover)
                            Text(" 입니다.", style = Typography.headlineLarge, color = TextPrimary)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "버튼을 누르면 바로 시작합니다.",
                            style = Typography.bodyLarge,
                            color = TextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 준비시간
                Text("준비시간", style = Typography.titleLarge, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                TimePicker(
                    hour = 0,
                    minute = prepMinute,
                    second = prepSecond,
                    onTimeSelected = { _, m, s ->
                        prepMinute = m
                        prepSecond = s
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 발화시간
                Text("발화시간", style = Typography.titleLarge, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                TimePicker(
                    hour = 0,
                    minute = speakMinute,
                    second = speakSecond,
                    onTimeSelected = { _, m, s ->
                        speakMinute = m
                        speakSecond = s
                    }
                )
            }

            // 시작 버튼
            CommonButton(
                text = "시작하기",
                size = ButtonSize.LG,
                onClick = {
                    onNext(prepMinute, speakMinute)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        }
    }
}

