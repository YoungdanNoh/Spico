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
import com.a401.spicoandroid.BuildConfig
import com.a401.spicoandroid.common.ui.theme.*
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun SpeechTestScreen(navController: NavController) {

    val context = LocalContext.current
    var sttResult by remember { mutableStateOf("") }
    var sttHistory by remember { mutableStateOf("") } // 누적용 변수
    var errorMessage by remember { mutableStateOf("") }

    var azureResult by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

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
                        //audioPath = audioRecorder.startRecording(context)
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

                val coroutineScope = rememberCoroutineScope()

                Button(
                    onClick = {
                        // 차후 presignedUrl은 실제 minio에서 사용할 파일의 url로
                        val presignedUrl = "https://k12a401.p.ssafy.io:8010/video/practice_10_final.mp4?response-content-type=video%2Fmp4&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=RFfWRR3OcLUyNiXWMBxZ%2F20250516%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250516T012016Z&X-Amz-Expires=21600&X-Amz-SignedHeaders=host&X-Amz-Signature=3cf300fe293f1a1c1211bc9057f3aba78094c7983e7bdc3f3f5c912fed8da194"
                        // referenceText는 대본으로 수정 필요
                        val referenceText = "자생한방병원입니다."

                        isProcessing = true
                        errorMessage = ""

                        val azureService = AzurePronunciationEvaluator(
                            subscriptionKey = BuildConfig.AZURE_KEY,
                            region = BuildConfig.AZURE_REGION
                        )

                        coroutineScope.launch {
                            try {

                                val wavFile = WavDownloader.downloadWavFile(presignedUrl)
                                val result = azureService.evaluatePronunciation(wavFile, referenceText, context)

                                val json = JSONObject(result)
                                val nBestArray = json.getJSONArray("NBest")
                                if (nBestArray.length() == 0) {
                                    throw Exception("NBest 항목이 없습니다.")
                                }

                                val bestResult = nBestArray.getJSONObject(0)

                                if (!bestResult.has("PronunciationAssessment")) {
                                    throw Exception("PronunciationAssessment 항목이 없습니다.")
                                }

                                val scoreJson = bestResult.getJSONObject("PronunciationAssessment")
                                val overall = scoreJson.getDouble("PronScore")

                                // 2. referenceText vs DisplayText 비교
                                val displayText = json.getString("DisplayText").replace("[.\\s]".toRegex(), "")
                                val expectedText = referenceText.replace("[.\\s]".toRegex(), "")
                                val isMatch = displayText == expectedText

                                azureResult = """
                                    총점 (Pronunciation Score): ${overall.toInt()} / 100
                                    기준 문장과 일치 여부: ${if (isMatch) "일치" else "불일치"}
                                """.trimIndent()

                                Log.d("AzurePronunciation", "발음평가 성공")
                            } catch (e: Exception) {
                                Log.d("AzurePronunciation", "발음평가 실패")
                                errorMessage = "발음 평가 실패: ${e.localizedMessage}"
                            } finally {
                                isProcessing = false
                            }
                        }
                    },
                    enabled = !isProcessing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isProcessing) Disabled else MaterialTheme.colorScheme.primary,
                        contentColor = White
                    )
                ) {
                    Text(text = if (isProcessing) "처리 중..." else "발음 평가 실행")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (azureResult.isNotEmpty()) {
                    Text(
                        text = azureResult,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }

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
