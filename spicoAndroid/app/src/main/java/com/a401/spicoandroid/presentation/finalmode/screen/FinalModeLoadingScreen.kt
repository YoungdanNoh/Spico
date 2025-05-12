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

enum class FinalModeLoadingType {
    QUESTION,
    REPORT
}

@Composable
fun FinalModeLoadingScreen(
    navController: NavController,
    viewModel: FinalModeViewModel = hiltViewModel(),
    type: FinalModeLoadingType
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        when (type) {
            FinalModeLoadingType.QUESTION -> {
                val result = viewModel.fetchQuestion()
                delay(1500)
                navController.navigate(NavRoutes.FinalModeQnA.withQuestion(result))
            }

            FinalModeLoadingType.REPORT -> {
                delay(2000)
                navController.navigate(NavRoutes.FinalModeReport.route)
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

