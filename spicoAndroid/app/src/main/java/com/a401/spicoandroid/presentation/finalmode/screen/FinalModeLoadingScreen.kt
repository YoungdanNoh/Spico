package com.a401.spicoandroid.presentation.finalmode.screen

import android.net.Uri
import androidx.compose.runtime.*
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.LoadingInProgressView
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.a401.spicoandroid.data.finalmode.dto.AnswerDto
import com.a401.spicoandroid.data.finalmode.dto.FinalModeResultRequestDto
import com.a401.spicoandroid.data.finalmode.dto.PauseRecordDto
import com.a401.spicoandroid.data.finalmode.dto.SpeedRecordDto
import com.a401.spicoandroid.data.finalmode.dto.VolumeRecordDto
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel

enum class FinalModeLoadingType {
    QUESTION,
    REPORT
}

@Composable
fun FinalModeLoadingScreen(
    navController: NavController,
    parentNavController: NavController,
    projectId: Int,
    practiceId: Int,
    viewModel: FinalModeViewModel = hiltViewModel(),
    type: FinalModeLoadingType
) {
    val context = LocalContext.current

    // 뒤로 가기 막기
    BackHandler(enabled = true){}

    LaunchedEffect(Unit) {
        Log.d("FinalFlow", "⏳ FinalModeLoadingScreen 진입: type=$type, projectId=$projectId, practiceId=$practiceId")

        when (type) {
            FinalModeLoadingType.QUESTION -> {
                Log.d("FinalFlow", "🚀 질문 생성 시작")
                viewModel.generateFinalQuestions(
                    projectId = projectId,
                    practiceId = practiceId,
                    speechContent = "Today, I will talk about the importance of communication skills in public speaking." // TODO: 실제 STT 결과로 교체
                )
                delay(1500)
                navController.navigate(NavRoutes.FinalModeQnA.withArgs(projectId, practiceId))
            }

            FinalModeLoadingType.REPORT -> {
                Log.d("FinalFlow", "📤 결과 전송 시작")

                viewModel.setPracticeId(practiceId)

                val questions = viewModel.finalQuestionState.value.questions

                val freshQuestions = viewModel.finalQuestionState.value.questions

                val answers = if (!viewModel.getHasQnA()) {
                    emptyList()
                } else if (freshQuestions.isEmpty()) {
                    listOf(
                        AnswerDto(questionId = 1, answer = "This is a default answer."),
                        AnswerDto(questionId = 2, answer = "Another default answer.")
                    )
                } else {
                    freshQuestions.mapIndexed { index, question ->
                        AnswerDto(
                            questionId = question.id,
                            answer = when (index) {
                                0 -> "That's a good question."
                                1 -> "Here's my second answer."
                                else -> "Thank you for asking."
                            }
                        )
                    }
                }

                val request = FinalModeResultRequestDto(
                    fileName = "temp_video.mp4",
                    speechContent = "Today, I will talk about the importance of communication skills in public speaking.",
                    pronunciationScore = 85,
                    pauseCount = 2,
                    pauseScore = 80,
                    speedScore = 90,
                    speedStatus = "FAST",
                    volumeScore = 88,
                    volumeStatus = "LOUD",
                    volumeRecords = listOf(
                        VolumeRecordDto("2025-05-17T15:00:00Z", "2025-05-17T15:00:05Z", "LOUD")
                    ),
                    speedRecords = listOf(
                        SpeedRecordDto("2025-05-17T15:00:06Z", "2025-05-17T15:00:10Z", "FAST")
                    ),
                    pauseRecords = listOf(
                        PauseRecordDto("2025-05-17T15:00:11Z", "2025-05-17T15:00:12Z")
                    ),
                    answers = answers
                )

                Log.d("FinalFlow", "📦 전송 request = $request")
                

                viewModel.submitFinalModeResult(
                    projectId = projectId,
                    request = request
                )

                delay(2000)
                parentNavController.navigate(
                    NavRoutes.FinalReport.createRoute(
                        projectId = projectId,
                        practiceId = practiceId
                    )
                )
            }
        }
    }

    val (imageRes, message) = when (type) {
        FinalModeLoadingType.QUESTION -> R.drawable.character_home_1 to
                "질문 생성중입니다.\n숨을 고르고 답변을 생각해주세요."
        FinalModeLoadingType.REPORT -> R.drawable.character_home_5 to
                "리포트를 정리 중이에요.\n잠시만 기다려주세요!"
    }

    LoadingInProgressView(
        imageRes = imageRes,
        message = message
    )
}


