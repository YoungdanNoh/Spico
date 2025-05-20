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
import com.a401.spicoandroid.domain.finalmode.model.toFinalModeResultRequestDto
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
    practiceViewModel: PracticeViewModel = hiltViewModel(),
    type: FinalModeLoadingType
) {
    val context = LocalContext.current
    val result by viewModel.assessmentResult.collectAsState()
    val questionState by viewModel.finalQuestionState.collectAsState()
    val isAnswerCompleted by viewModel.isAnswerCompleted.collectAsState()
    val answerTimeLimit = practiceViewModel.answerTimeLimit

    // 뒤로 가기 막기
    BackHandler(enabled = true){}

// 질문 생성용 LaunchedEffect
    if (type == FinalModeLoadingType.QUESTION) {
        LaunchedEffect(result) {
            result?.let {
                Log.d("FinalFlow", "🚀 질문 생성 시작")
                Log.d("TimerDebug", "📥 LoadingScreen에서 answerTimeLimit 주입 전 값: ${practiceViewModel.answerTimeLimit}")
                viewModel.setAnswerTimeLimit(practiceViewModel.answerTimeLimit)

                viewModel.generateFinalQuestions(
                    projectId = projectId,
                    practiceId = practiceId,
                    speechContent = it.transcribedText
                )
                delay(1500)
                navController.navigate(NavRoutes.FinalModeQnA.withArgs(projectId, practiceId))
            }
        }
    }

    // 결과 전송용 LaunchedEffect
    if (type == FinalModeLoadingType.REPORT) {
        LaunchedEffect(Unit) {
            Log.d("FinalFlow", "📤 결과 전송 시작")
            Log.d("FinalFlow", "📝 현재 저장된 답변: ${questionState.answers}")

            viewModel.setPracticeId(practiceId)

            val answers: List<AnswerDto> = questionState.questions.map { question ->
                val answer = questionState.answers.find { it.questionId == question.id }?.text ?: ""
                Log.d("FinalFlow", "📝 질문 ${question.id}의 답변: $answer")
                AnswerDto(
                    questionId = question.id,
                    answer = answer
                )
            }

            Log.d("FinalFlow", "📦 전송할 답변 목록: $answers")

            viewModel.submitFinalModeResult(
                projectId = projectId,
                request = result!!.toFinalModeResultRequestDto(answers = answers)
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


