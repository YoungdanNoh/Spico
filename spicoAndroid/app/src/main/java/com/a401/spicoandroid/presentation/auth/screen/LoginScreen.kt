package com.a401.spicoandroid.presentation.auth.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.a401.spicoandroid.R
import com.a401.spicoandroid.common.ui.theme.Hover
import com.a401.spicoandroid.common.ui.theme.TextPrimary
import com.a401.spicoandroid.infrastructure.datastore.UserDataStore
import com.a401.spicoandroid.presentation.auth.component.*
import com.a401.spicoandroid.presentation.auth.viewmodel.LoginViewModel
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    userDataStore: UserDataStore
) {
    val imageList = listOf(
        R.drawable.img_login1,
        R.drawable.img_login2,
        R.drawable.img_login3,
        R.drawable.img_login4,
        R.drawable.character_login
    )

    val textList = listOf(
        listOf("코칭모드" to Hover, "로 실시간\n" to TextPrimary, "AI발표 분석을 받을 수 있어요!" to TextPrimary),
        listOf("파이널 모드" to Hover, "로 실전처럼\n" to TextPrimary, "발표하고 " to TextPrimary, "Q&A" to Hover, "까지" to TextPrimary),
        listOf("성량, 발음, 속도 등 한눈에\n" to TextPrimary, "확인하는 발표 " to TextPrimary, "리포트" to Hover),
        listOf("여러 주제를 접할 수 있는\n" to TextPrimary, "랜덤스피치" to Hover),
        listOf("Spico" to Hover, "와 함께\n" to TextPrimary, "완벽한 발표를 준비해봐요!" to TextPrimary)
    )

    val pagerState = rememberPagerState(initialPage = 0) { imageList.size }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val loginSuccess by loginViewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            println("🔥 로그인 성공 - 이동 시작")

            // ✅ suspend 함수는 이렇게 coroutineScope 안에서 실행
            var token: String? = null
            while (token == null) {
                token = userDataStore.observeAccessToken().first()
                delay(50)
            }

            println("✅ 토큰 반영 완료 후 홈으로 이동 → $token")

            navController.navigate(NavRoutes.Home.route) {
                popUpTo(0) { inclusive = true }
            }

            loginViewModel.resetLoginState()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000L)
            scope.launch {
                val next = (pagerState.currentPage + 1) % imageList.size
                pagerState.scrollToPage(next)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures {
                    scope.launch {
                        val next = (pagerState.currentPage + 1) % imageList.size
                        pagerState.scrollToPage(next)
                    }
                }
            }
    ) {
        // 인디케이터
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.TopCenter)
        ) {
            LoginPagerContent(pagerState, imageList, textList)
            Spacer(modifier = Modifier.height(140.dp))
        }

        // 로그인 버튼
        LoginBottomSection(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            currentPage = pagerState.currentPage,
            totalCount = imageList.size,
            onSelect = { scope.launch { pagerState.scrollToPage(it) } },
            onKakaoLoginClick = {
                handleKakaoLogin(context, loginViewModel)
            }
        )
    }
}

private fun handleKakaoLogin(context: Context, loginViewModel: LoginViewModel) {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (token != null) {
            loginViewModel.onLoginClicked(token.accessToken)
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                if (error is ClientError && error.reason.name == "Cancelled") return@loginWithKakaoTalk
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                loginViewModel.onLoginClicked(token.accessToken)
            }
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}