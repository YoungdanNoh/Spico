package com.a401.spicoandroid.presentation.randomspeech.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.*
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.common.utils.getTopicKor
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.a401.spicoandroid.presentation.randomspeech.component.PrepTimePickerDialog
import com.a401.spicoandroid.presentation.randomspeech.component.SpeakTimePickerDialog
import com.a401.spicoandroid.presentation.randomspeech.viewmodel.RandomSpeechSharedViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RandomSpeechSettingScreen(
    navController: NavController,
    viewModel: RandomSpeechSharedViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val topic = uiState.topic

    if (topic == null) {
        // topic 설정 전까지 UI 보류
        Box(modifier = Modifier.fillMaxSize().background(White))
        return
    }

    val topicKor = getTopicKor(topic.name)

    var prepMinute by remember { mutableIntStateOf(uiState.prepTime / 60) }
    var prepSecond by remember { mutableIntStateOf(0) }
    var speakMinute by remember { mutableIntStateOf(uiState.speakTime / 60) }
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
                    minute = prepMinute,
                    second = prepSecond,
                    onTimeSelected = { _, m, s ->
                        prepMinute = m
                        prepSecond = s
                    },
                    validate = { m, _ ->
                        when {
                            m < 1 -> "준비시간은 최소 1분입니다."
                            m > 10 -> "준비시간은 최대 10분입니다."
                            else -> null
                        }
                    },
                    guideText = "준비시간은 분 단위로 설정됩니다."
                ) { show, onDismiss, onTimeSelected ->
                    if (show) {
                        PrepTimePickerDialog(
                            initialMinute = prepMinute,
                            onDismiss = onDismiss,
                            onMinuteSelected = { m -> onTimeSelected(0, m, 0) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 발화시간
                Text("발화시간", style = Typography.titleLarge, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                TimePicker(
                    minute = speakMinute,
                    second = speakSecond,
                    onTimeSelected = { _, m, s ->
                        speakMinute = m
                        speakSecond = s
                    },
                    validate = { m, _ ->
                        when {
                            m < 1 -> "발화시간은 최소 1분입니다."
                            m > 20 -> "발화시간은 최대 20분입니다."
                            else -> null
                        }
                    },
                    guideText = "발화시간은 분 단위로 설정됩니다."
                ) { show, onDismiss, onTimeSelected ->
                    if (show) {
                        SpeakTimePickerDialog(
                            initialMinute = speakMinute,
                            onDismiss = onDismiss,
                            onMinuteSelected = { m -> onTimeSelected(0, m, 0) }
                        )
                    }
                }
            }

            // 시작 버튼
            CommonButton(
                text = "시작하기",
                size = ButtonSize.LG,
                onClick = {
                    viewModel.setTime(prepMinute * 60, speakMinute * 60)
                    navController.navigate(NavRoutes.RandomSpeechReady.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        }
    }
}