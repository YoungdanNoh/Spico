package com.a401.spicoandroid.presentation.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.a401.spicoandroid.presentation.mypage.component.WithdrawAlert
import com.a401.spicoandroid.presentation.mypage.viewmodel.MyPageViewModel

@Composable
fun MyPageScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isAlertVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CommonTopBar(
                centerText = "프로필"
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.character_coaching),
                contentDescription = "스피코 캐릭터 이미지",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 16.dp)
            )

            Text(text = "닉네임", style = Typography.headlineLarge, color = TextPrimary)
            Text(
                text = state.name,
                style = Typography.bodyLarge,
                color = TextTertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Placeholder, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )

            Text(text = "이메일", style = Typography.headlineLarge, color = TextPrimary)
            Text(
                text = state.email,
                style = Typography.bodyLarge,
                color = TextTertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Placeholder, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                CommonButton(
                    text = "로그아웃",
                    size = ButtonSize.MD,
                    backgroundColor = TextTertiary,
                    borderColor = White,
                    textColor = White,
                    onClick = viewModel::onLogoutClicked
                )
                CommonButton(
                    text = "계정탈퇴",
                    size = ButtonSize.MD,
                    backgroundColor = Error,
                    borderColor = White,
                    textColor = White,
                    onClick = { isAlertVisible = true }
                )
            }
        }

        if (isAlertVisible) {
            WithdrawAlert(
                onConfirm = {
                    isAlertVisible = false
                    viewModel.confirmWithdraw()
                },
                onDismiss = {
                    isAlertVisible = false
                }
            )
        }
    }
}
