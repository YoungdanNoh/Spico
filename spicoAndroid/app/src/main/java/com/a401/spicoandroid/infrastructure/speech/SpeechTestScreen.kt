package com.a401.spicoandroid.infrastructure.speech

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.NavRoutes

@Composable
fun SpeechTestScreen(navController: NavController) {

    val context = LocalContext.current
    var sttResult by remember { mutableStateOf("") }
    var sttHistory by remember { mutableStateOf("") } // 누적용 변수
    var errorMessage by remember { mutableStateOf("") }

    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            errorMessage = "마이크 권한이 필요합니다."
        }
    }

    // 컴포저블 시작할 때 한 번 권한 요청
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    val googleStt  = remember {
        GoogleStt(
            context = context,
            onResult = { result ->
                sttResult = result
                sttHistory += if (sttHistory.isBlank()) result else "\n$result" // 줄바꿈 누적
            },
            onError = { error -> errorMessage = error }
        )
    }

    SpeakoAndroidTheme {
        Scaffold(
            containerColor = BackgroundPrimary
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpeechStartButton(
                    onClick = {
                        Log.d("SpeechTestScreen", "STT 시작 버튼 클릭됨")
                        // 여기에 STT 시작 로직 연결
                        googleStt.start()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("결과: $sttResult")
                if (errorMessage.isNotEmpty()) {
                    Text("에러: $errorMessage", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                StopSttButton(onClick = {
                    googleStt.stop()
                })

                Spacer(modifier = Modifier.height(8.dp))

                Text("현재 인식 결과: $sttResult")
                val scrollState = rememberScrollState()

                Text(
                    text = sttHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .verticalScroll(scrollState)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ReturnHomeButton {
                    navController.navigate(NavRoutes.Home.route)
                }
            }
        }
    }
}

@Composable
fun SpeechStartButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Action else Disabled,
            contentColor = White,
            disabledContainerColor = Disabled,
            disabledContentColor = TextTertiary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "시작하기",
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Composable
fun StopSttButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Error,
            contentColor = White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "stt 종료하기",
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Composable
fun ReturnHomeButton(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "홈으로 돌아가기",
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
        )
    }
}
