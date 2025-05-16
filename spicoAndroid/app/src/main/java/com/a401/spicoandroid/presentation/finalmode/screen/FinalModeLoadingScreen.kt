package com.a401.spicoandroid.presentation.finalmode.screen

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.component.LoadingInProgressView
import com.a401.spicoandroid.presentation.finalmode.viewmodel.FinalModeViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.a401.spicoandroid.presentation.practice.viewmodel.PracticeViewModel

enum class FinalModeLoadingType {
    QUESTION,
    REPORT
}

@Composable
fun FinalModeLoadingScreen(
    navController: NavController,
    viewModel: FinalModeViewModel = hiltViewModel(),
    practiceViewModel: PracticeViewModel = hiltViewModel(),
    type: FinalModeLoadingType
) {
    val context = LocalContext.current
    val practiceIdState = practiceViewModel.practiceId.collectAsState()
    val practiceId = practiceIdState.value

    val projectId = practiceViewModel.selectedProject?.id

    LaunchedEffect(practiceId) {
        when (type) {
            FinalModeLoadingType.QUESTION -> {
                viewModel.generateFinalQuestions(
                    projectId = projectId ?: -1,
                    practiceId = practiceId ?: -1,
                    speechContent = "Hello everyone, my name is John." // TODO: STT 결과로 교체
                )
                delay(1500)
                navController.navigate(NavRoutes.FinalModeQnA.route)
            }

            FinalModeLoadingType.REPORT -> {
                delay(2000)
                if (projectId != null && practiceId != null) {
                    navController.navigate(
                        NavRoutes.FinalReport.createRoute(
                            projectId = projectId,
                            practiceId = practiceId
                        )
                    )
                } else {
                    navController.navigate(NavRoutes.ProjectList.route)
                }
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
