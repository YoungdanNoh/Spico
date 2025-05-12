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

@Composable
fun FinalModeLoadingScreen(
    navController: NavController,
    viewModel: FinalModeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var question by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val result = viewModel.fetchQuestion()
        question = result
        delay(1500) // UX를 위한 짧은 대기
        navController.navigate(NavRoutes.FinalModeQnA.withQuestion(result))
    }

    LoadingInProgressView(
        imageRes = R.drawable.character_home_1,
        message = "질문 생성중입니다.\n숨을 고르고 답변을 생각해주세요."
    )
}
